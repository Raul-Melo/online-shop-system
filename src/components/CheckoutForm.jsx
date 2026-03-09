import { useState } from 'react';
import { validateCheckout } from '../utils/validations';

export default function CheckoutForm({
    onSubmit,
    isSubmitting = false,
    apiError = null
}) {
    const [formData, setFormData] = useState({
        nome: '',
        email: '',
        endereco: '',
        formaPagamento: ''
    });

    const [errors, setErrors] = useState({});

    function handleChange(event) {
        const { name, value } = event.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    }

    function handleSubmit(event) {
        event.preventDefault();

        const validationErrors = validateCheckout(formData);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) return;

        onSubmit(formData);
    }

    return (
        <form onSubmit={handleSubmit} className="rounded-lg border bg-white p-6 shadow-sm">
            <h2 className="mb-4 text-lg font-semibold">Dados para finalizar compra</h2>

            {apiError && (
                <div className="mb-4 rounded-md border border-red-200 bg-red-50 p-3 text-sm text-red-700">
                    {apiError}
                </div>
            )}

            <div className="mb-4">
                <label className="mb-1 block font-medium">Nome</label>
                <input
                    type="text"
                    name="nome"
                    value={formData.nome}
                    onChange={handleChange}
                    className="w-full rounded-md border px-3 py-2"
                />
                {errors.nome && <p className="mt-1 text-sm text-red-600">{errors.nome}</p>}
            </div>

            <div className="mb-4">
                <label className="mb-1 block font-medium">E-mail</label>
                <input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    className="w-full rounded-md border px-3 py-2"
                />
                {errors.email && <p className="mt-1 text-sm text-red-600">{errors.email}</p>}
            </div>

            <div className="mb-4">
                <label className="mb-1 block font-medium">Endereço</label>
                <textarea
                    name="endereco"
                    value={formData.endereco}
                    onChange={handleChange}
                    rows={4}
                    className="w-full rounded-md border px-3 py-2"
                />
                {errors.endereco && (
                    <p className="mt-1 text-sm text-red-600">{errors.endereco}</p>
                )}
            </div>

            <div className="mb-6">
                <label className="mb-1 block font-medium">Forma de pagamento</label>
                <select
                    name="formaPagamento"
                    value={formData.formaPagamento}
                    onChange={handleChange}
                    className="w-full rounded-md border px-3 py-2"
                >
                    <option value="">Selecione</option>
                    <option value="PIX">PIX</option>
                    <option value="CARTAO">CARTAO</option>
                    <option value="BOLETO">BOLETO</option>
                </select>
                {errors.formaPagamento && (
                    <p className="mt-1 text-sm text-red-600">{errors.formaPagamento}</p>
                )}
            </div>

            <button
                type="submit"
                disabled={isSubmitting}
                className="w-full rounded-md bg-green-600 px-4 py-3 text-white hover:bg-green-700 disabled:cursor-not-allowed disabled:bg-slate-400"
            >
                {isSubmitting ? 'Enviando pedido...' : 'Finalizar compra'}
            </button>
        </form>
    );
}