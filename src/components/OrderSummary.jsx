import { formatCurrency } from '../utils/currency';

export default function OrderSummary({
    items,
    total,
    title = 'Resumo do pedido'
}) {
    return (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
            <h2 className="mb-4 text-lg font-semibold">{title}</h2>

            <div className="space-y-3">
                {items.map((item) => (
                    <div key={item.id} className="flex items-start justify-between gap-4">
                        <div>
                            <p className="font-medium">{item.nome}</p>
                            <p className="text-sm text-slate-600">
                                {item.quantidade} x {formatCurrency(item.precoUnitario)}
                            </p>
                        </div>
                        <div className="font-medium">
                            {formatCurrency(item.quantidade * item.precoUnitario)}
                        </div>
                    </div>
                ))}
            </div>

            <div className="mt-4 border-t pt-4">
                <div className="flex items-center justify-between text-lg font-bold">
                    <span>Total</span>
                    <span>{formatCurrency(total)}</span>
                </div>
            </div>
        </div>
    );
}