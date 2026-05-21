const API_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const gameId = urlParams.get('id');

    console.log('📄 Game_page.html загружена');
    console.log('🔍 Полный URL:', window.location.href);
    console.log('🎮 ID из URL:', gameId);

    if (!gameId) {
        console.error('❌ ID игры не найден в URL!');
        document.getElementById('gameTitle').textContent = 'Игра не найдена';
        return;
    }

    // Проверяем, что gameId - число
    const id = parseInt(gameId);
    if (isNaN(id)) {
        console.error('❌ Неверный формат ID:', gameId);
        document.getElementById('gameTitle').textContent = 'Неверный ID игры';
        return;
    }

    await loadGame(id);
});

async function loadGame(gameId) {
    console.log(`📡 Запрос к API: ${API_URL}/games/${gameId}`);

    try {
        const response = await fetch(`${API_URL}/games/${gameId}`);
        console.log('📥 Статус ответа:', response.status);

        if (response.status === 404) {
            throw new Error('Игра не найдена в базе данных');
        }

        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }

        const game = await response.json();
        console.log('✅ Игра загружена:', game);

        renderGame(game);

    } catch (error) {
        console.error('❌ Ошибка загрузки:', error);
        document.getElementById('gameTitle').textContent = 'Ошибка загрузки';
        document.getElementById('gameDescription').textContent = error.message;
    }
}

function renderGame(game) {
    console.log('🎨 Отрисовка игры:', game.title);

    // Заголовок
    document.getElementById('gameTitle').textContent = game.title;

    // Описание
    document.getElementById('gameDescription').textContent = game.description || 'Описание отсутствует';

    // Разработчик и издатель
    document.getElementById('gameDeveloper').textContent = game.developer || 'Неизвестно';
    document.getElementById('gamePublisher').textContent = game.publisher || 'Неизвестно';

    // Дата выхода
    if (game.releaseDate) {
        const date = new Date(game.releaseDate);
        document.getElementById('gameReleaseDate').textContent = date.toLocaleDateString('ru-RU');
    } else {
        document.getElementById('gameReleaseDate').textContent = 'Неизвестно';
    }

    // Рейтинг
    const rating = game.rating ? Math.round(game.rating * 100) : 'N/A';
    document.getElementById('gameRating').textContent = rating;

    // Обложка
    const coverUrl = game.imageUrl?.startsWith('http')
        ? game.imageUrl
        : (game.imageUrl || 'https://via.placeholder.com/400x600?text=No+Cover');

    const coverImg = document.getElementById('gameCover');
    coverImg.src = coverUrl;
    coverImg.alt = game.title;
    console.log('🖼️ Обложка:', coverUrl);

    // Теги (жанры)
    const tagsContainer = document.getElementById('gameTags');
    if (game.genreIds && game.genreIds.length > 0) {
        tagsContainer.innerHTML = game.genreIds.map(id =>
            `<span class="game-tag">Genre #${id}</span>`
        ).join('');
    } else {
        const year = game.releaseDate ? new Date(game.releaseDate).getFullYear() : '2020';
        tagsContainer.innerHTML = `
            <span class="game-tag">Action</span>
            <span class="game-tag">${year}</span>
            <span class="game-tag">PC</span>
        `;
    }

    // Скриншоты
    const screenshotsTrack = document.getElementById('screenshotsTrack');
    if (screenshotsTrack) {
        screenshotsTrack.innerHTML = `
            <div class="screenshot-item">
                <img src="${coverUrl}" alt="Screenshot 1">
            </div>
            <div class="screenshot-item">
                <img src="${coverUrl}" alt="Screenshot 2">
            </div>
            <div class="screenshot-item">
                <img src="${coverUrl}" alt="Screenshot 3">
            </div>
        `;
    }

    console.log('✅ Игра отрисована');
}

// Кнопка опубликовать
document.getElementById('publishBtn')?.addEventListener('click', () => {
    const text = document.getElementById('reviewText').value;
    if (text.trim()) {
        alert('Отзыв опубликован! (Пока заглушка)');
        document.getElementById(    'reviewText').value = '';
    } else {
        alert('Введите текст отзыва');
    }
});