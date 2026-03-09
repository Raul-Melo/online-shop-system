import { formatCurrency } from '../utils/currency';

export default function ProductCard({
    product,
    availableStock,
    quantityInCart = 0,
    onAddToCart
}) {
    const isOutOfStock = availableStock <= 0;

    return (
        <div className="flex h-full flex-col rounded-lg border bg-white p-4 shadow-sm">
            <img
                src={product.imagem}
                alt={product.nome}
                className="mb-4 h-48 w-full rounded-md bg-slate-100 object-contain"
            />

            <h3 className="mb-2 text-lg font-semibold">{product.nome}</h3>
            <p className="mb-3 text-sm text-slate-600">{product.descricao}</p>

            <div className="mb-2 text-lg font-bold">{formatCurrency(product.preco)}</div>

            <div className="mb-1 text-sm text-slate-500">
                Estoque disponível: {availableStock}
            </div>

            {quantityInCart > 0 && (
                <div className="mb-4 text-sm text-blue-600">
                    No carrinho: {quantityInCart}
                </div>
            )}

            {quantityInCart === 0 && <div className="mb-4" />}

            <button
                onClick={() => onAddToCart(product)}
                disabled={isOutOfStock}
                className="mt-auto rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-slate-400"
            >
                {isOutOfStock ? 'Sem estoque' : 'Adicionar ao carrinho'}
            </button>
        </div>
    );
}