export function validateCheckout(values) {
    const errors = {};

    if (!values.nome.trim()) {
        errors.nome = 'Nome é obrigatório.';
    }

    if (!values.email.trim()) {
        errors.email = 'E-mail é obrigatório.';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email)) {
        errors.email = 'E-mail inválido.';
    }

    if (!values.endereco.trim()) {
        errors.endereco = 'Endereço é obrigatório.';
    }

    const validPayments = ['PIX', 'CARTAO', 'BOLETO'];
    if (!values.formaPagamento || !validPayments.includes(values.formaPagamento)) {
        errors.formaPagamento = 'Selecione uma forma de pagamento válida.';
    }

    return errors;
}