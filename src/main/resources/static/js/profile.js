// src/main/resources/static/js/profile.js

const API_URL = 'http://localhost:8080';

// Загрузка профиля при открытии страницы
document.addEventListener('DOMContentLoaded', loadProfile);

async function loadProfile() {
    const token = localStorage.getItem('accessToken');
    if (!token) {
        window.location.href = '/Login_and_Registration.html';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/me`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 401) {
            const refreshed = await refreshAuthToken();
            if (refreshed) return loadProfile();
            window.location.href = '/Login_and_Registration.html';
            return;
        }

        if (!response.ok) throw new Error('Failed to load profile');

        const user = await response.json();
        populateForm(user);

    } catch (error) {
        console.error('Profile load error:', error);
        alert('Ошибка загрузки профиля');
    }
}

// Заполнение формы данными пользователя (под твой HTML!)
function populateForm(user) {
    // Поля формы
    document.getElementById('nickname').value = user.nickname || '';
    document.getElementById('username').value = user.username || '';
    document.getElementById('email').value = user.email || '';
    document.getElementById('bio').value = user.description || '';

    // Аватар (если есть URL)
    if (user.avatarUrl) {
        document.querySelector('.avatar-img').src = user.avatarUrl;
    }

    // Отображение в шапке профиля
    document.querySelector('.profile-nickname').textContent = user.nickname || user.username;
    document.querySelector('.profile-email').textContent = user.email || '';
}

// Обработка сохранения формы
document.querySelector('.btn-save')?.addEventListener('click', async (e) => {
    e.preventDefault(); // Предотвращаем стандартную отправку формы

    const token = localStorage.getItem('accessToken');
    const btn = document.querySelector('.btn-save');
    const originalText = btn.textContent;

    btn.textContent = 'Сохранение...';
    btn.disabled = true;

    // Отправляем ТОЛЬКО изменяемые поля (username и email — readOnly)
    const payload = {
        nickname: document.getElementById('nickname').value,
        description: document.getElementById('bio').value,
        avatarUrl: document.querySelector('.avatar-img')?.src || null
    };

    try {
        const response = await fetch(`${API_URL}/me/profile`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const updatedUser = await response.json();
            populateForm(updatedUser); // Обновляем отображение
            alert('✅ Профиль успешно обновлён!');
        } else {
            const error = await response.json();
            alert(`❌ Ошибка: ${error.description || 'Не удалось сохранить'}`);
        }
    } catch (error) {
        console.error('Save error:', error);
        alert('Ошибка подключения к серверу');
    } finally {
        btn.textContent = originalText;
        btn.disabled = false;
    }
});

// Обработка загрузки аватара (опционально)
document.getElementById('avatar-input')?.addEventListener('change', async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    // Здесь можно добавить загрузку файла на сервер
    // Пока просто предпросмотр:
    const reader = new FileReader();
    reader.onload = (event) => {
        document.querySelector('.avatar-img').src = event.target.result;
    };
    reader.readAsDataURL(file);
});

// Обновление токена
async function refreshAuthToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) return false;

    try {
        const response = await fetch(`${API_URL}/auth/refresh`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken })
        });

        if (!response.ok) return false;

        const tokens = await response.json();
        localStorage.setItem('accessToken', tokens.token);
        localStorage.setItem('refreshToken', tokens.refreshToken);
        return true;
    } catch {
        return false;
    }
}