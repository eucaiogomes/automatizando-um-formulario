// script.js
        // Validação simples de frontend para os testes locais
        document.addEventListener('DOMContentLoaded', function () {
          const form = document.querySelector('.form-signin');
          const email = document.getElementById('inputEmail');
          const pass = document.getElementById('inputPassword');
          const texto = document.getElementById('texto');

          form.addEventListener('submit', function (e) {
            e.preventDefault(); // evita recarregar a página ao submeter localmente
            const eVal = email.value.trim();
            const pVal = pass.value.trim();

            if (eVal === 'Lucas@Lucas' && pVal === '131415') {
              texto.textContent = 'Login realizado com sucesso';
              texto.style.color = 'green';
            } else {
              texto.textContent = 'Usuário ou senha inválidos';
              texto.style.color = 'crimson';
            }
          });
        });
   