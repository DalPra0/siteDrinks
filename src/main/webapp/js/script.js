const API_BASE_URL = '/drinkmenu/api'; // Matches Tomcat application context + servlet pattern

function displayDrinks(drinks, containerId) {
    const container = document.getElementById(containerId);
    if (!container) {
        console.error(`Container with id ${containerId} not found.`);
        return;
    }
    container.innerHTML = ''; // Clear previous content

    if (!drinks || drinks.length === 0) {
        container.innerHTML = '<p>Nenhum drink encontrado.</p>';
        return;
    }

    drinks.forEach(drink => {
        const card = document.createElement('div');
        card.className = 'drink-card';

        const image = document.createElement('img');
        image.src = drink.imageUrl || 'https://via.placeholder.com/300x200?text=No+Image'; // Placeholder if no image
        image.alt = drink.name;

        const name = document.createElement('h3');
        name.textContent = drink.name;

        const ingredients = document.createElement('p');
        ingredients.innerHTML = `<strong>Ingredientes:</strong> ${drink.ingredients || 'N/A'}`;

        const rating = document.createElement('p');
        rating.className = 'rating';
        rating.textContent = `Avaliação: ${'★'.repeat(drink.rating || 0)}${'☆'.repeat(5 - (drink.rating || 0))}`;

        card.appendChild(image);
        card.appendChild(name);
        card.appendChild(ingredients);
        card.appendChild(rating);
        container.appendChild(card);
    });
}

async function loadLatestDrinks(limit = 4) {
    try {
        const response = await fetch(`${API_BASE_URL}/drinks?limit=${limit}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const drinks = await response.json();
        displayDrinks(drinks, 'latest-drinks-container');
    } catch (error) {
        console.error('Erro ao carregar últimos drinks:', error);
        const container = document.getElementById('latest-drinks-container');
        if (container) {
            container.innerHTML = '<p>Erro ao carregar drinks. Tente novamente mais tarde.</p>';
        }
    }
}

async function loadAllDrinks() {
    try {
        const response = await fetch(`${API_BASE_URL}/drinks`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const drinks = await response.json();
        displayDrinks(drinks, 'all-drinks-container');
    } catch (error) {
        console.error('Erro ao carregar todos os drinks:', error);
        const container = document.getElementById('all-drinks-container');
        if (container) {
            container.innerHTML = '<p>Erro ao carregar drinks. Tente novamente mais tarde.</p>';
        }
    }
}

function handleFormSubmission() {
    const form = document.getElementById('add-drink-form');
    if (!form) return;

    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const messageEl = document.getElementById('form-message');
        messageEl.textContent = ''; // Clear previous messages

        const formData = new FormData(form);
        const drinkData = {
            name: formData.get('name'),
            ingredients: formData.get('ingredients'),
            instructions: formData.get('instructions'),
            imageUrl: formData.get('imageUrl'),
            rating: parseInt(formData.get('rating'), 10)
        };

        try {
            const response = await fetch(`${API_BASE_URL}/add-drink`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(drinkData),
            });

            const result = await response.json();

            if (response.ok && response.status === 201) {
                messageEl.style.color = 'green';
                messageEl.textContent = result.message || 'Drink adicionado com sucesso!';
                form.reset();
                setTimeout(() => { // Optional: Redirect after a delay
                    window.location.href = 'drinks.html';
                }, 2000);
            } else {
                messageEl.style.color = 'red';
                messageEl.textContent = result.message || `Erro: ${response.statusText}`;
            }
        } catch (error) {
            console.error('Erro ao submeter formulário:', error);
            messageEl.style.color = 'red';
            messageEl.textContent = 'Erro de conexão ao tentar adicionar o drink.';
        }
    });
}