import { useEffect, useState } from 'react';
import ProductCard from '../components/ProductCard';
import LoadingState from '../components/LoadingState';
import ErrorState from '../components/ErrorState';
import EmptyState from '../components/EmptyState';
import { getProducts } from '../services/productService';
import { useCart } from '../hooks/useCart';

export default function ProductsPage() {
    const [products, setProducts] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const { addItem, getItemQuantity } = useCart();

    async function loadProducts() {
        try {
            setIsLoading(true);
            setError(null);
            const data = await getProducts();
            setProducts(data);
        } catch {
            setError('Não foi possível carregar os produtos.');
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        loadProducts();
    }, []);

    if (isLoading) {
        return <LoadingState message="Carregando produtos..." />;
    }

    if (error) {
        return <ErrorState message={error} onRetry={loadProducts} />;
    }

    if (products.length === 0) {
        return (
            <EmptyState
                title="Nenhum produto encontrado"
                description="Não existem produtos disponíveis no momento."
            />
        );
    }

    return (
        <section>
            <h1 className="mb-6 text-2xl font-bold">Produtos</h1>

            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                {products.map((product) => {
                    const quantityInCart = getItemQuantity(product.id);
                    const availableStock = Math.max(product.estoque - quantityInCart, 0);

                    return (
                        <ProductCard
                            key={product.id}
                            product={product}
                            availableStock={availableStock}
                            quantityInCart={quantityInCart}
                            onAddToCart={addItem}
                        />
                    );
                })}
            </div>
        </section>
    );
}