const form = document.getElementById("formSolicitacao");

form.addEventListener("submit", function (evento) {
    evento.preventDefault();

    const tipo = document.getElementById("tipo").value;
    const descricao = document.getElementById("descricao").value;
    const local = document.getElementById("local").value;
    const prioridade = document.getElementById("prioridade").value;

    const solicitacao = {
        tipoSolicitacao: tipo,
        prioridade: prioridade,
        descricao: descricao,
        localizacao: local
    };

    const resultado = document.getElementById("resultado");
    resultado.textContent = "Solicitação enviada: " + tipo;
});

const perfil = document.getElementById("perfil");

perfil.addEventListener("change", function () {
    const opcaoMausTratos = document.getElementById("opcaoMausTratos");
    const camposCadastrado = document.getElementById("camposCadastrado");

    if (perfil.value === "ANONIMO") {
        opcaoMausTratos.style.display = "none";
        camposCadastrado.style.display = "none";
    } else {
        opcaoMausTratos.style.display = "block";
        camposCadastrado.style.display = "block";
    }
});