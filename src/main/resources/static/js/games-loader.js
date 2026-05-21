// src/main/resources/static/js/games-loader.js

const API_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', async () => {
    const gamesGrid = document.getElementById('gamesGrid');

    if (!gamesGrid) {
        console.error('❌ gamesGrid не найден!');
        return;
    }

    try {
        console.log('🎮 Загрузка игр...');
        const response = await fetch(`${API_URL}/games`);

        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        const games = await response.json();
        console.log(`✅ Загружено игр: ${games.length}`);
        console.log('📋 Игры:', games);  // ← Посмотри ID в консоли

        renderGames(games, gamesGrid);

    } catch (error) {
        console.error('❌ Ошибка:', error);
        gamesGrid.innerHTML = '<p style="color:red;text-align:center">Не удалось загрузить игры</p>';
    }
});

function renderGames(games, container) {
    if (!games?.length) {
        container.innerHTML = '<p style="text-align:center;color:#888">Игры не найдены</p>';
        return;
    }

    const visible = games.slice(0, 6);

    container.innerHTML = visible.map(game => {
        const year = game.releaseDate ? new Date(game.releaseDate).getFullYear() : '';
        const imgUrl = game.imageUrl?.startsWith('http')
            ? game.imageUrl
            : (game.imageUrl || 'https://via.placeholder.com/300x400?text=No+Image');

        const title = escapeHtml(game.title);
        const developer = escapeHtml(game.developer);
        const rating = game.rating ? Math.round(game.rating * 10) : 'N/A';
        const gameId = game.id;

        console.log(`🎮 Рендерим игру: ID=${gameId}, Title=${title}`);  // ← Лог

        return `
        <div class="game-card" onclick="navigateToGame(${gameId})" style="cursor: pointer;">
            <div class="game-image-wrapper">
                <img src="${imgUrl}" 
                     alt="${title}" 
                     class="game-image"
                     onerror="this.src='https://via.placeholder.com/300x400?text=No+Image'">
                <span class="game-rating">${rating}</span>
            </div>
            <div class="game-info">
                <h3 class="game-title">${title}</h3>
                <div class="game-meta">
                    ${year ? `<span class="game-year">${year}</span>` : ''}
                    <span class="game-genre">${developer}</span>
                </div>
            </div>
        </div>`;
    }).join('');

    console.log('✅ Игры отрисованы');
}

// ✅ Глобальная функция для навигации
window.navigateToGame = function(gameId) {
    console.log(`🔗 Переход к игре ID=${gameId}`);
    window.location.href = `/Game_page.html?id=${gameId}`;
};

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}