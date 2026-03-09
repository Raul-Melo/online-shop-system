import { Link, Navigate, useLocation } from 'react-router-dom';

export default function OrderSuccessPage() {
    const location = useLocation();
    const state = location.state;

    if (!state?.orderId || !state?.numeroPedido) {
        return <Navigate to="/products" replace />;
    }

    return (
        <section className="mx-auto max-w-2xl">
            <div className="rounded-lg border bg-white p-8 shadow-sm">
                <h1 className="mb-4 text-2xl font-bold text-green-700">
                    Pedido realizado com sucesso
                </h1>

                <div className="space-y-3 text-slate-700">
                    <p>
                        <strong>Número do pedido:</strong> {state.numeroPedido}
                    </p>
                    <p>
                        <strong>ID do pedido:</strong> {state.orderId}
                    </p>
                </div>

                <div className="mt-6 flex flex-col gap-3 sm:flex-row">
                    <Link
                        to={`/orders/${state.orderId}`}
                        className="rounded-md bg-blue-600 px-4 py-3 text-center text-white hover:bg-blue-700"
                    >
                        Ver detalhes do pedido
                    </Link>

                    <Link
                        to="/products"
                        className="rounded-md border px-4 py-3 text-center hover:bg-slate-50"
                    >
                        Voltar para produtos
                    </Link>
                </div>
            </div>
        </section>
    );
}