const usuarios = [
    {
        email: "atendente@sistema.com",
        senha: "atendente123",
        nome: "Atendente",
        tipo: "USUARIO_ATENDENTE"
    },
    {
        email: "gestor@sistema.com",
        senha: "gestor123",
        nome: "Gestor",
        tipo: "USUARIO_GESTOR"
    }
];

const form = document.getElementById("formLogin");

form.addEventListener("submit", function (evento) {
    evento.preventDefault();

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    const resultado = document.getElementById("resultado");

    if (email === "" || senha === "") {
        resultado.textContent = "Preencha email e senha.";
        return;
    }

    const usuario = usuarios.find(function (u) {
        return u.email === email && u.senha === senha;
    });

    if (usuario) {
            document.getElementById("tituloPendentes").style.display = "block";
            mostrarPendentes();
    } else {
        resultado.textContent = "Email ou senha inválidos.";
    }
});

const pendentes = [
    {
        protocolo: "OS202561",
        tipo: "DENUNCIA_BURACO",
        descricao: "Buraco grande na avenida",
        status: "PENDENTE"
    },
    {
        protocolo: "OS202562",
        tipo: "SOLICITACAO_PODA",
        descricao: "Árvore cobrindo o poste",
        status: "PENDENTE"
    }
];

function mostrarPendentes() {
    const lista = document.getElementById("listaPendentes");

    let html = "";

pendentes.forEach(function (s) {
        html = html + "<div class='card-solicitacao'>";
        html = html + "<p>Protocolo: " + s.protocolo + "</p>";
        html = html + "<p>Tipo: " + s.tipo + "</p>";
        html = html + "<p>Descrição: " + s.descricao + "</p>";
        html = html + "<p>Status: " + s.status + "</p>";
        html = html + "<select data-protocolo='" + s.protocolo + "'>";
        html = html + "<option value='PENDENTE'" + (s.status === "PENDENTE" ? " selected" : "") + ">Pendente</option>";
        html = html + "<option value='EM_ATENDIMENTO'" + (s.status === "EM_ATENDIMENTO" ? " selected" : "") + ">Em atendimento</option>";
        html = html + "<option value='AGUARDANDO_RESPOSTA'" + (s.status === "AGUARDANDO_RESPOSTA" ? " selected" : "") + ">Aguardando resposta</option>";
        html = html + "<option value='CONCLUIDA'" + (s.status === "CONCLUIDA" ? " selected" : "") + ">Concluída</option>";
        html = html + "</select>";
        html = html + "</div>";
    });

    lista.innerHTML = html;
}

    const listaPendentes = document.getElementById("listaPendentes");

    listaPendentes.addEventListener("change", function (evento) {
     const protocolo = evento.target.getAttribute("data-protocolo");
     const novoStatus = evento.target.value;

    if (protocolo) {
            const observacao = prompt("Observação (obrigatória):");

            if (observacao === null || observacao.trim() === "") {
                mostrarPendentes();
                return;
            }

            const solicitacao = pendentes.find(function (s) {
                return s.protocolo === protocolo;
            });

            solicitacao.status = novoStatus;

            mostrarPendentes();
        }
});