const solicitacoes = [
    {
        protocolo: "OS202561",
        tipo: "DENUNCIA_BURACO",
        status: "EM_ATENDIMENTO",
        prioridade: "ALTA",
        descricao: "Buraco grande na avenida",
        localizacao: "Av Brasil",
        prazo: "10/06/2026"
    },
    {
        protocolo: "OS202562",
        tipo: "SOLICITACAO_PODA",
        status: "PENDENTE",
        prioridade: "MEDIA",
        descricao: "Árvore cobrindo o poste",
        localizacao: "Rua das Flores",
        prazo: "13/06/2026"
    }
];

const botao = document.getElementById("botaoBuscar");

botao.addEventListener("click", function () {

const protocoloDigitado = document.getElementById("protocolo").value;

const encontrada = solicitacoes.find(function (s) {
        return s.protocolo === protocoloDigitado;
    });

    const resultado = document.getElementById("resultado");

        if (encontrada) {
                resultado.innerHTML =
                    "<p>Protocolo: " + encontrada.protocolo + "</p>" +
                    "<p>Tipo: " + encontrada.tipo + "</p>" +
                    "<p>Status: " + encontrada.status + "</p>" +
                    "<p>Prioridade: " + encontrada.prioridade + "</p>" +
                    "<p>Localização: " + encontrada.localizacao + "</p>" +
                    "<p>Descrição: " + encontrada.descricao + "</p>" +
                    "<p>Prazo: " + encontrada.prazo + "</p>";
            } else {
                resultado.textContent = "Protocolo não encontrado.";
            }
});

