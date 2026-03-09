import { createContext, useCallback, useContext, useMemo, useState } from 'react';

const CartContext = createContext(undefined);

export function CartProvider({ children }) {
    const [items, setItems] = useState([]);

    const addItem = useCallback((product) => {
        setItems((currentItems) => {
            const existingItem = currentItems.find((item) => item.id === product.id);

            if (!existingItem) {
                return [...currentItems, { ...product, quantity: 1 }];
            }

            if (existingItem.quantity >= product.estoque) {
                return currentItems;
            }

            return currentItems.map((item) =>
                item.id === product.id
                    ? { ...item, quantity: item.quantity + 1 }
                    : item
            );
        });
    }, []);

    const removeItem = useCallback((productId) => {
        setItems((currentItems) =>
            currentItems.filter((item) => item.id !== productId)
        );
    }, []);

    const increaseQuantity = useCallback((productId) => {
        setItems((currentItems) =>
            currentItems.map((item) => {
                if (item.id !== productId) return item;
                if (item.quantity >= item.estoque) return item;
                return { ...item, quantity: item.quantity + 1 };
            })
        );
    }, []);

    const decreaseQuantity = useCallback((productId) => {
        setItems((currentItems) =>
            currentItems.map((item) => {
                if (item.id !== productId) return item;
                if (item.quantity <= 1) return item;
                return { ...item, quantity: item.quantity - 1 };
            })
        );
    }, []);

    const clearCart = useCallback(() => {
        setItems([]);
    }, []);

    const getItemQuantity = useCallback((productId) => {
        const item = items.find((item) => item.id === productId);
        return item ? item.quantity : 0;
    }, [items]);

    const totalItems = useMemo(
        () => items.reduce((acc, item) => acc + item.quantity, 0),
        [items]
    );

    const totalAmount = useMemo(
        () => items.reduce((acc, item) => acc + item.preco * item.quantity, 0),
        [items]
    );

    const value = useMemo(
        () => ({
            items,
            addItem,
            removeItem,
            increaseQuantity,
            decreaseQuantity,
            clearCart,
            getItemQuantity,
            totalItems,
            totalAmount
        }),
        [
            items,
            addItem,
            removeItem,
            increaseQuantity,
            decreaseQuantity,
            clearCart,
            getItemQuantity,
            totalItems,
            totalAmount
        ]
    );

    return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCartContext() {
    const context = useContext(CartContext);

    if (!context) {
        throw new Error('useCartContext must be used within CartProvider');
    }

    return context;
}