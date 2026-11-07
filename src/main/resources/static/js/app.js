const API_URL = '/api/students';
const $ = (selector, root = document) => root.querySelector(selector);
const escapeHtml = value => String(value ?? '').replace(/[&<>"']/g, char => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[char]));
let students = [];
let editingRollNo = null;

async function request(url, options = {}) {
  const response = await fetch(url, options);
  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json') ? await response.json() : await response.text();
  if (!response.ok) {
    const message = typeof body === 'string' ? body : body.error || Object.values(body).join(' ');
    throw new Error(message || `Request failed (${response.status})`);
  }
  return body;
}

function showAlert(target, message, type = 'danger') {
  if (!target) return;
  target.innerHTML = `<div class="notice notice-${type}" role="status">${escapeHtml(message)}</div>`;
}

function calculateAge(dob) {
  if (!dob) return '—';
  const birth = new Date(`${dob}T00:00:00`);
  if (Number.isNaN(birth.getTime())) return '—';
  const today = new Date();
  let age = today.getFullYear() - birth.getFullYear();
  const month = today.getMonth() - birth.getMonth();
  if (month < 0 || (month === 0 && today.getDate() < birth.getDate())) age--;
  return age >= 0 ? `${age} yrs` : '—';
}

function fullName(student) {
  return student.name || [student.firstName, student.lastName].filter(Boolean).join(' ');
}

function renderAdminStudents() {
  const body = $('#admin-students');
  if (!body) return;
  const query = ($('#admin-search')?.value || '').trim().toLowerCase();
  const filtered = students.filter(s => [s.rollNo, fullName(s), s.email, s.phoneNumber, s.collegeName, s.courseName, s.branchName].some(v => String(v ?? '').toLowerCase().includes(query)));
  $('#student-count').textContent = String(students.length);
  body.innerHTML = filtered.length ? filtered.map(s => `<tr>
    <td class="roll-cell">${escapeHtml(s.rollNo)}</td><td><strong class="name-cell">${escapeHtml(fullName(s))}</strong><small class="sub-cell">${escapeHtml(s.branchName)}</small></td>
    <td><a class="email-link" href="mailto:${escapeHtml(s.email)}">${escapeHtml(s.email)}</a><small class="sub-cell">${escapeHtml(s.phoneNumber)}</small></td>
    <td><span class="name-cell">${escapeHtml(s.collegeName)}</span><small class="sub-cell">${escapeHtml(s.courseName)}</small></td><td><span class="age-pill">${escapeHtml(calculateAge(s.dob))}</span></td>
    <td class="actions-cell"><button class="icon-action" data-action="edit" data-roll="${escapeHtml(s.rollNo)}" aria-label="Edit ${escapeHtml(fullName(s))}" title="Edit">✎</button><button class="icon-action delete-action" data-action="delete" data-roll="${escapeHtml(s.rollNo)}" aria-label="Delete ${escapeHtml(fullName(s))}" title="Delete">⌫</button></td></tr>`).join('') : '<tr><td colspan="6" class="empty-state">No student records found.</td></tr>';
}

async function loadStudents() {
  const adminBody = $('#admin-students');
  if (!adminBody) return;
  if (adminBody) adminBody.innerHTML = '<tr><td colspan="6" class="empty-state">Loading student records…</td></tr>';
  try {
    students = await request(API_URL);
    renderAdminStudents();
  } catch (error) {
    if (adminBody) adminBody.innerHTML = '<tr><td colspan="6" class="empty-state">Could not load records. Check that the database is available.</td></tr>';
    showAlert($('#admin-alert'), error.message);
  }
}

function initRegistration() {
  const form = $('#registration-form');
  if (!form) return;
  const dob = $('#dob');
  dob.max = new Date().toISOString().slice(0, 10);
  form.addEventListener('submit', async event => {
    event.preventDefault();
    const payload = Object.fromEntries(new FormData(form).entries());
    payload.rollNo = Number(payload.rollNo);
    try {
      await request(API_URL, {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload)});
      form.reset();
      dob.max = new Date().toISOString().slice(0, 10);
      showAlert($('#public-alert'), 'Registration submitted successfully.', 'success');
    } catch (error) {
      showAlert($('#public-alert'), error.message || 'Could not submit registration.');
    }
  });
}

function initLogin() {
  const form = $('#login-form');
  if (!form) return;
  if (sessionStorage.getItem('easy-academy-admin') === 'true') window.location.replace('/studentdetailsmanagement.html');
  form.addEventListener('submit', event => {
    event.preventDefault();
    const username = $('#username').value;
    const password = $('#password').value;
    if (username === 'Admin' && password === 'Admin') {
      sessionStorage.setItem('easy-academy-admin', 'true');
      window.location.href = '/studentdetailsmanagement.html';
    } else showAlert($('#login-alert'), 'The username or password is incorrect.');
  });
}

const editFields = [
  ['rollNo','Roll number','number'],['firstName','First name','text'],['lastName','Last name','text'],['dob','Date of birth','date'],
  ['email','Email address','email'],['phoneNumber','Phone number','tel'],['address','Home address','textarea'],
  ['collegeName','College name','text'],['courseName','Course name','text'],['branchName','Field / Branch','text'],['admissionDate','Date of admission','date'],['previousEducation','Previous educational details','textarea']
];

function openEdit(student) {
  editingRollNo = student.rollNo;
  const container = $('#edit-fields');
  container.innerHTML = editFields.map(([key,label,type]) => {
    const wide = ['address','previousEducation'].includes(key) ? ' full-width' : '';
    const value = escapeHtml(student[key] ?? '');
    const input = type === 'textarea' ? `<textarea class="control" id="edit-${key}" name="${key}" rows="3" required>${value}</textarea>` : `<input class="control" id="edit-${key}" name="${key}" type="${type}" value="${value}" ${key === 'rollNo' ? 'readonly' : ''} ${type === 'number' ? 'min="1"' : ''} required>`;
    return `<label class="field${wide}"><span>${label}</span>${input}</label>`;
  }).join('');
  $('#edit-modal').classList.remove('d-none');
  document.body.classList.add('modal-open');
  $('#edit-firstName').focus();
}

function closeEdit() {
  $('#edit-modal')?.classList.add('d-none');
  document.body.classList.remove('modal-open');
}

function initAdmin() {
  if (!$('#admin-students')) return;
  if (sessionStorage.getItem('easy-academy-admin') !== 'true') {
    window.location.replace('/loginpage.html');
    return;
  }
  $('#logout-button')?.addEventListener('click', () => { sessionStorage.removeItem('easy-academy-admin'); window.location.href = '/loginpage.html'; });
  $('#admin-search')?.addEventListener('input', renderAdminStudents);
  $('#admin-students').addEventListener('click', async event => {
    const button = event.target.closest('button[data-action]');
    if (!button) return;
    const student = students.find(s => String(s.rollNo) === button.dataset.roll);
    if (!student) return;
    if (button.dataset.action === 'edit') { openEdit(student); return; }
    if (!window.confirm(`Delete the record for ${fullName(student)} (roll ${student.rollNo})?`)) return;
    try { await request(`${API_URL}/${student.rollNo}`, {method:'DELETE'}); showAlert($('#admin-alert'), 'Student record deleted.', 'success'); await loadStudents(); }
    catch (error) { showAlert($('#admin-alert'), error.message); }
  });
  $('#close-edit').addEventListener('click', closeEdit);
  $('#cancel-edit').addEventListener('click', closeEdit);
  $('#edit-modal').addEventListener('click', event => { if (event.target.id === 'edit-modal') closeEdit(); });
  document.addEventListener('keydown', event => { if (event.key === 'Escape') closeEdit(); });
  $('#edit-form').addEventListener('submit', async event => {
    event.preventDefault();
    const payload = Object.fromEntries(new FormData(event.currentTarget).entries());
    payload.rollNo = Number(payload.rollNo);
    try { await request(`${API_URL}/${editingRollNo}`, {method:'PUT', headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload)}); closeEdit(); showAlert($('#admin-alert'), 'Student details updated.', 'success'); await loadStudents(); }
    catch (error) { showAlert($('#admin-alert'), error.message); }
  });
  loadStudents();
}

initRegistration();
initLogin();
initAdmin();
