const API_URL = '/api/students';
const studentList = document.querySelector('#student-list');
const studentForm = document.querySelector('#student-form');
const studentModal = new bootstrap.Modal(document.querySelector('#student-modal'));
let students = [];
let editingRollNo = null;

async function request(url, options = {}) {
    const response = await fetch(url, options);
    if (!response.ok) {
        let message = `Request failed (${response.status})`;
        try {
            const body = await response.json();
            message = body.error || Object.values(body).join(' ' ) || message;
        } catch (_) { /* Keep the status message for non-JSON errors. */ }
        throw new Error(message);
    }
    if (response.status === 204) return null;
    return response.json();
}

function showAlert(message, type = 'danger') {
    document.querySelector('#alert-area').innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">${escapeHtml(message)}<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button></div>`;
}

function escapeHtml(value) {
    return String(value ?? '').replace(/[&<>"']/g, character => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    })[character]);
}

function renderStudents() {
    const search = document.querySelector('#search-input').value.trim().toLowerCase();
    const course = document.querySelector('#course-filter').value.trim().toLowerCase();
    const filtered = students.filter(student =>
        [student.rollNo, student.name, student.email, student.course].some(value => String(value ?? '').toLowerCase().includes(search)) &&
        String(student.course ?? '').toLowerCase().includes(course)
    );

    if (!filtered.length) {
        studentList.innerHTML = '<tr><td colspan="5" class="text-center text-secondary py-4">No students found.</td></tr>';
        return;
    }

    studentList.innerHTML = filtered.map(student => `
        <tr>
            <td>${escapeHtml(student.rollNo)}</td>
            <td class="fw-medium">${escapeHtml(student.name)}</td>
            <td>${escapeHtml(student.email)}</td>
            <td><span class="badge text-bg-light">${escapeHtml(student.course)}</span></td>
            <td class="text-end text-nowrap">
                <button class="btn btn-sm btn-outline-primary me-1" data-action="edit" data-roll="${escapeHtml(student.rollNo)}">Edit</button>
                <button class="btn btn-sm btn-outline-danger" data-action="delete" data-roll="${escapeHtml(student.rollNo)}">Delete</button>
            </td>
        </tr>`).join('');
}

async function loadStudents() {
    studentList.innerHTML = '<tr><td colspan="5" class="text-center text-secondary py-4">Loading students…</td></tr>';
    try {
        students = await request(API_URL);
        renderStudents();
    } catch (error) {
        studentList.innerHTML = '<tr><td colspan="5" class="text-center text-secondary py-4">Could not load students.</td></tr>';
        showAlert(`${error.message}. Check that the database is available.`);
    }
}

function openForm(student = null) {
    studentForm.reset();
    editingRollNo = student?.rollNo ?? null;
    document.querySelector('#dialog-title').textContent = student ? 'Edit student' : 'Add student';
    document.querySelector('#save-button').textContent = student ? 'Save changes' : 'Save student';
    document.querySelector('#rollno-group').classList.toggle('d-none', Boolean(student));
    document.querySelector('#student-rollno').required = !student;
    if (student) {
        studentForm.elements.name.value = student.name;
        studentForm.elements.email.value = student.email;
        studentForm.elements.course.value = student.course;
    }
    studentModal.show();
}

document.querySelector('#add-student-button').addEventListener('click', () => openForm());
document.querySelector('#search-input').addEventListener('input', renderStudents);
document.querySelector('#course-filter').addEventListener('input', renderStudents);

studentForm.addEventListener('submit', async event => {
    event.preventDefault();
    const payload = {
        rollNo: editingRollNo ?? Number(studentForm.elements.rollNo.value),
        name: studentForm.elements.name.value.trim(),
        email: studentForm.elements.email.value.trim(),
        course: studentForm.elements.course.value.trim()
    };
    const url = editingRollNo === null ? API_URL : `${API_URL}/${editingRollNo}`;
    const method = editingRollNo === null ? 'POST' : 'PUT';
    try {
        await request(url, { method, headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
        studentModal.hide();
        showAlert(editingRollNo === null ? 'Student added successfully.' : 'Student updated successfully.', 'success');
        await loadStudents();
    } catch (error) {
        showAlert(error.message);
    }
});

studentList.addEventListener('click', async event => {
    const button = event.target.closest('button[data-action]');
    if (!button) return;
    const student = students.find(item => String(item.rollNo) === button.dataset.roll);
    if (!student) return;
    if (button.dataset.action === 'edit') {
        openForm(student);
        return;
    }
    if (!window.confirm(`Delete ${student.name}?`)) return;
    try {
        await request(`${API_URL}/${student.rollNo}`, { method: 'DELETE' });
        showAlert('Student deleted successfully.', 'success');
        await loadStudents();
    } catch (error) {
        showAlert(error.message);
    }
});

loadStudents();
