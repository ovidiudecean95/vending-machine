truncate products,users,coin_inventory;
ALTER SEQUENCE users_seq_id RESTART WITH 10000;
ALTER SEQUENCE products_seq_id RESTART WITH 10000;
ALTER SEQUENCE coin_inventory_seq_id RESTART WITH 10000;

insert into
    users (id, username, password, deposit, role, deleted)
values
    (1, 'buyer_account', '$2a$10$HTuWzf2h6QrRWnqmNhgzdeQP0IKxHGe8S32Ki75xz4/A0xsovsaSa', 0, 'ROLE_BUYER', false),
    (2, 'seller_account', '$2a$10$mW2WIeoWgjhyEFwOJTHgKedwMGhqk4O3YkTTE5ybQQ36IWoIlEpOS', 0, 'ROLE_SELLER', false),
    (3, 'seller_account_2', '$2a$10$mW2WIeoWgjhyEFwOJTHgKedwMGhqk4O3YkTTE5ybQQ36IWoIlEpOS', 0, 'ROLE_SELLER', false);

insert into
    products (id, amount_available, cost, product_name, seller_id, deleted)
values
    (1, 10, 50, 'product1', 2, false),
    (2, 0, 20, 'product2', 2, false),
    (3, 5, 100, 'product3', 2, false),
    (4, 1, 15, 'product4', 3, false),
    (5, 10, 30, 'product5', 3, false),
    (6, 1, 50, 'product6', 3, true);

insert into
    coin_inventory(id, coin_value, amount)
values
    (1, 5, 2),
    (2, 10, 2);
