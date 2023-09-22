// Função para realizar o login
function login() {
    // Obtém os valores de email e senha digitados pelo usuário
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;

    // Verifica se o email e senha foram preenchidos
    if (!email || !password) {
        alert('Por favor, preencha o email e a senha.');
        return;
    }

    // Realiza a autenticação com o Firebase Auth
    firebase.auth().signInWithEmailAndPassword(email, password)
        .then(function(userCredential) {
            // Autenticação bem-sucedida, redireciona para a página home.html
            window.location.href = 'pages/home.html';
        })
        .catch(function(error) {
            // Trata erros de autenticação
            var errorCode = error.code;
            var errorMessage = error.message;
            alert('Erro ao realizar o login: ' + errorMessage);
        });
}
