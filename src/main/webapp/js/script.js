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
        card.addEventListener('click', () => {
            window.location.href = `just-a-drink.html?id=${drink.id}`
        })
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

async function loadDrinkDetails(id) {
    const container = document.getElementById('drink-details');
    if (!container) return;

    try {
        const response = await fetch(`${API_BASE_URL}/drinks-detail?id=${id}`);
        if (!response.ok) throw new Error('Drink não encontrado');

        const drink = await response.json();

        container.innerHTML = `
      <h2>${drink.name}</h2>
      <img src="${drink.imageUrl || 'https://via.placeholder.com/300x200?text=No+Image'}" alt="${drink.name}">
      <p><strong>Ingredientes:</strong> ${drink.ingredients}</p>
      <p><strong>Instruções:</strong> ${drink.instructions}</p>
      <p><strong>Avaliação:</strong> ${'★'.repeat(drink.rating)}${'☆'.repeat(5 - drink.rating)}</p>
      <button id="edit-btn">Editar</button>
      <button id="delete-btn">Excluir</button>
      <div id="edit-form-container" style="display:none;"></div>
    `;

        document.getElementById('edit-btn').addEventListener('click', () => {
            showEditForm(drink);
        });

        document.getElementById('delete-btn').addEventListener('click', () => {
            if (confirm('Tem certeza que deseja excluir este drink?')) {
                deleteDrink(drink.id);
            }
        });

    } catch (error) {
        container.innerHTML = `<p>Erro ao carregar o drink: ${error.message}</p>`;
    }
}

function showEditForm(drink) {
    const modal = document.getElementById('edit-modal');

    modal.style.display = 'flex';

    const form = modal.querySelector('#edit-drink-form');

    // Preenche o formulário com os dados do drink
    form.name.value = drink.name;
    form.ingredients.value = drink.ingredients;
    form.instructions.value = drink.instructions;
    form.imageUrl.value = drink.imageUrl || '';
    form.rating.value = drink.rating || 0;

    const messageEl = modal.querySelector('#edit-form-message');
    messageEl.textContent = '';

    // Cancelar botão fecha a modal
    modal.querySelector('#cancel-edit-btn').onclick = () => {
        modal.style.display = 'none';
    };

    form.onsubmit = async (e) => {
        e.preventDefault();

        const updatedDrink = {
            id: drink.id,
            name: form.name.value,
            ingredients: form.ingredients.value,
            instructions: form.instructions.value,
            imageUrl: form.imageUrl.value,
            rating: parseInt(form.rating.value, 10)
        };

        try {
            const response = await fetch(`${API_BASE_URL}/drinks-detail`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedDrink)
            });

            const result = await response.json();

            if (response.ok) {
                messageEl.style.color = 'green';
                messageEl.textContent = 'Drink atualizado com sucesso!';
                modal.style.display = 'none';
                loadDrinkDetails(drink.id);
            } else {
                messageEl.style.color = 'red';
                messageEl.textContent = result.message || 'Erro ao atualizar.';
            }
        } catch (error) {
            console.error(error);
            messageEl.style.color = 'red';
            messageEl.textContent = 'Erro ao atualizar o drink.';
        }
    };
}


async function deleteDrink(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/drinks-detail?id=${id}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            alert('Drink excluído com sucesso!');
            window.location.href = 'drinks.html'; // Redireciona para a lista
        } else {
            alert('Erro ao excluir o drink.');
        }
    } catch (error) {
        alert('Erro ao excluir o drink.');
        console.error(error);
    }
}
