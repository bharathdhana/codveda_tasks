CREATE TABLE IF NOT EXISTS books (
                                     book_id SERIAL PRIMARY KEY,
                                     title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    total_copies INT NOT NULL CHECK (total_copies >= 0),
    available_copies INT NOT NULL CHECK (
                                            available_copies >= 0 AND available_copies <= total_copies
                                        )
    );

CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(15),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            transaction_id SERIAL PRIMARY KEY,
                                            book_id INT NOT NULL REFERENCES books(book_id),
    user_id INT NOT NULL REFERENCES users(user_id),
    borrow_date DATE NOT NULL DEFAULT CURRENT_DATE,
    return_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'BORROWED',
    CONSTRAINT valid_transaction_status
    CHECK (status IN ('BORROWED', 'RETURNED')),
    CONSTRAINT valid_return_date
    CHECK (return_date IS NULL OR return_date >= borrow_date),
    CONSTRAINT valid_transaction_dates
    CHECK (
(status = 'BORROWED' AND return_date IS NULL)
    OR (status = 'RETURNED' AND return_date IS NOT NULL)
    )
    );

CREATE INDEX IF NOT EXISTS idx_transactions_user ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_book ON transactions(book_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions(status);