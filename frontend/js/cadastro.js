const form = document.getElementById("formCadastro");

form.addEventListener("submit", function (evento) {
    evento.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    const resultado = document.getElementById("resultado");

    if (nome === "" || email === "" || senha === "") {
        resultado.textContent = "Preencha todos os campos.";
        return;
    }

    const novoUsuario = {
        nome: nome,
        email: email,
        senha: senha,
        tipo: "USUARIO_LOGADO"
    };

    resultado.textContent = "Usuário cadastrado: " + nome;
    document.getElementById("linkSolicitar").style.display = "block";
});