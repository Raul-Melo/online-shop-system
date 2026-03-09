import { useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import CheckoutForm from '../components/CheckoutForm';
import OrderSummary from '../components/OrderSummary';
import { useCart } from '../hooks/useCart';
import { createOrder } from '../services/orderService';

export default function CheckoutPage() {
    const { items, totalAmount, clearCart } = useCart();
    const navigate = useNavigate();

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [apiError, setApiError] = useState(null);
    const [orderCompleted, setOrderCompleted] = useState(false);

    if (items.length === 0 && !orderCompleted) {
        return <Navigate to="/cart" replace />;
    }

    async function handleSubmit(data) {
        const payload = {
            nome: data.nome,
            email: data.email,
            endereco: data.endereco,
            formaPagamento: data.formaPagamento,
            produtos: items.map((item) => ({
                productId: item.id,
                quantity: item.quantity
            }))
        };

        try {
            setIsSubmitting(true);
            setApiError(null);

            const response = await createOrder(payload);

            console.log('CREATE ORDER RESPONSE:', response);

            setOrderCompleted(true);

            navigate('/order/success', {
                state: {
                    orderId: response.id,
                    numeroPedido: response.numeroPedido
                }
            });

            clearCart();
        } catch (error) {
            console.error('Erro ao criar pedido:', error);
            console.error('Resposta da API:', error?.response?.data);
            setApiError(
                error?.response?.data?.message ||
                'Não foi possível finalizar o pedido. Tente novamente.'
            );
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <section>
            <h1 className="mb-6 text-2xl font-bold">Checkout</h1>

            <div className="grid gap-6 lg:grid-cols-[2fr_1fr]">
                <CheckoutForm
                    onSubmit={handleSubmit}
                    isSubmitting={isSubmitting}
                    apiError={apiError}
                />

                <OrderSummary
                    items={items.map((item) => ({
                        id: item.id,
                        nome: item.nome,
                        quantidade: item.quantity,
                        precoUnitario: item.preco
                    }))}
                    total={totalAmount}
                />
            </div>
        </section>
    );
}