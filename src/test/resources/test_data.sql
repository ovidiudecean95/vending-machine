truncate products, users;

insert into
    users (id, username, password, deposit, role, deleted)
values
    (1, 'buyer_account', '$2a$10$pkjBkpP5JieYa34lBglfPe1kEtuVZwZ5SmLGSmj3atY/xVVaeeAVO', 0, 'ROLE_BUYER', false),
    (2, 'seller_account', '$2a$10$.wMh2FlTzkIeWgTJFAJmZugrhdlpa6LgqEw7QnsXKgTLS63MjxMfK', 0, 'ROLE_SELLER', false);