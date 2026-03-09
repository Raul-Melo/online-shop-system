export default function LoadingState({ message = 'Carregando...' }) {
    return (
        <div className="rounded-lg border bg-white p-8 text-center shadow-sm">
            <div className="mb-3 text-sm text-slate-500">{message}</div>
        </div>
    );
}