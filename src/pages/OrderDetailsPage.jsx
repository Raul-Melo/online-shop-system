import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import LoadingState from '../components/LoadingState';
import ErrorState from '../components/ErrorState';
import { getOrderById } from '../services/orderService';
import { formatCurrency } from '../utils/currency';

export default function OrderDetailsPage() {
    const { id } = useParams();

    const [order, setOrder] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    async function loadOrder() {
        if (!id) return;

        try {
            setIsLoading(true);
            setError(null);
            const data = await getOrderById(id);
            setOrder(data);
        } catch {
            setError('Não foi possível carregar os detalhes do pedido.');
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        loadOrder();
    }, [id]);

    if (isLoading) {
        return <LoadingState message="Carregando pedido..." />;
    }

    if (error) {
        return <ErrorState message={error} onRetry={loadOrder} />;
    }

    if (!order) {
        return <ErrorState message="Pedido não encontrado." />;
    }

    return (
        <section className="space-y-6">
            <h1 className="text-2xl font-bold">Detalhes do pedido</h1>

            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <div className="grid gap-4 md:grid-cols-2">
                    <p>
                        <strong>Número do pedido:</strong> {order.numeroPedido}
                    </p>
                    <p>
                        <strong>ID:</strong> {order.id}
                    </p>
                    <p>
                        <strong>Nome:</strong> {order.nome}
                    </p>
                    <p>
                        <strong>E-mail:</strong> {order.email}
                    </p>
                    <p>
                        <strong>Endereço:</strong> {order.endereco}
                    </p>
                    <p>
                        <strong>Forma de pagamento:</strong> {order.formaPagamento}
                    </p>
                    <p>
                        <strong>Status:</strong> {order.status}
                    </p>
                    <p>
                        <strong>Criado em:</strong>{' '}
                        {new Date(order.criadoEm).toLocaleString('pt-BR')}
                    </p>
                </div>
            </div>

            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <h2 className="mb-4 text-lg font-semibold">Itens do pedido</h2>

                <div className="overflow-x-auto">
                    <table className="min-w-full border-collapse">
                        <thead>
                            <tr className="border-b text-left">
                                <th className="px-2 py-3">Produto</th>
                                <th className="px-2 py-3">Preço unitário</th>
                                <th className="px-2 py-3">Quantidade</th>
                                <th className="px-2 py-3">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            {order.itens.map((item) => (
                                <tr key={item.id} className="border-b">
                                    <td className="px-2 py-3">{item.nomeProduto}</td>
                                    <td className="px-2 py-3">{formatCurrency(item.precoUnitario)}</td>
                                    <td className="px-2 py-3">{item.quantidade}</td>
                                    <td className="px-2 py-3">{formatCurrency(item.subtotal)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="mt-6 text-right text-xl font-bold">
                    Total: {formatCurrency(order.total)}
                </div>
            </div>
        </section>
    );
}