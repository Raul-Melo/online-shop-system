import { Link } from 'react-router-dom';

export default function EmptyState({
    title,
    description,
    actionLabel,
    actionTo
}) {
    return (
        <div className="rounded-lg border bg-white p-8 text-center shadow-sm">
            <h2 className="mb-2 text-xl font-semibold">{title}</h2>
            <p className="mb-4 text-slate-600">{description}</p>
            {actionLabel && actionTo && (
                <Link
                    to={actionTo}
                    className="inline-block rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                >
                    {actionLabel}
                </Link>
            )}
        </div>
    );
}