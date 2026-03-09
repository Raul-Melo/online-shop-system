import { formatCurrency } from '../utils/currency';

export default function CartItemRow({
    item,
    onIncrease,
    onDecrease,
    onRemove
}) {
    const subtotal = item.preco * item.quantity;

    return (
        <div className="flex flex-col gap-4 rounded-lg border bg-white p-4 shadow-sm md:flex-row md:items-center">
            <img
                src={item.imagem}
                alt={item.nome}
                className="h-24 w-24 rounded-md bg-slate-100 object-contain"
            />

            <div className="flex-1">
                <h3 className="font-semibold">{item.nome}</h3>
                <p className="text-sm text-slate-600">
                    Preço unitário: {formatCurrency(item.preco)}
                </p>
                <p className="text-sm text-slate-600">Estoque: {item.estoque}</p>
            </div>

            <div className="flex items-center gap-2">
                <button
                    onClick={() => onDecrease(item.id)}
                    disabled={item.quantity <= 1}
                    className="rounded-md border px-3 py-1 disabled:cursor-not-allowed disabled:opacity-50"
                >
                    -
                </button>
                <span className="min-w-8 text-center">{item.quantity}</span>
                <button
                    onClick={() => onIncrease(item.id)}
                    disabled={item.quantity >= item.estoque}
                    className="rounded-md border px-3 py-1 disabled:cursor-not-allowed disabled:opacity-50"
                >
                    +
                </button>
            </div>

            <div className="min-w-28 text-right font-semibold">
                {formatCurrency(subtotal)}
            </div>

            <button
                onClick={() => onRemove(item.id)}
                className="rounded-md bg-red-600 px-4 py-2 text-white hover:bg-red-700"
            >
                Remover
            </button>
        </div>
    );
}