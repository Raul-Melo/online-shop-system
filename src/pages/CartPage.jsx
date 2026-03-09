import { Link } from 'react-router-dom';
import CartItemRow from '../components/CartItemRow';
import OrderSummary from '../components/OrderSummary';
import EmptyState from '../components/EmptyState';
import { useCart } from '../hooks/useCart';

export default function CartPage() {
    const {
        items,
        increaseQuantity,
        decreaseQuantity,
        removeItem,
        totalAmount
    } = useCart();

    if (items.length === 0) {
        return (
            <EmptyState
                title="Seu carrinho está vazio"
                description="Adicione produtos para continuar."
                actionLabel="Ver produtos"
                actionTo="/products"
            />
        );
    }

    return (
        <section>
            <h1 className="mb-6 text-2xl font-bold">Carrinho</h1>

            <div className="grid gap-6 lg:grid-cols-[2fr_1fr]">
                <div className="space-y-4">
                    {items.map((item) => (
                        <CartItemRow
                            key={item.id}
                            item={item}
                            onIncrease={increaseQuantity}
                            onDecrease={decreaseQuantity}
                            onRemove={removeItem}
                        />
                    ))}
                </div>

                <div className="space-y-4">
                    <OrderSummary
                        items={items.map((item) => ({
                            id: item.id,
                            nome: item.nome,
                            quantidade: item.quantity,
                            precoUnitario: item.preco
                        }))}
                        total={totalAmount}
                    />

                    <Link
                        to="/checkout"
                        className="block rounded-md bg-green-600 px-4 py-3 text-center text-white hover:bg-green-700"
                    >
                        Ir para checkout
                    </Link>
                </div>
            </div>
        </section>
    );
}