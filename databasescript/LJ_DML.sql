-- vilka kunder som handlar vita adidas
select Specification.color, Specification.brand, Customer.firstName
from CustomerOrder
inner join Customer on CustomerOrder.customerId = Customer.id
inner join OrderedProduct on CustomerOrder.id = OrderedProduct.orderId
inner join Product on OrderedProduct.productId = Product.id
inner join Specification on Product.specId = Specification.id
where Specification.color = 'White' and Specification.brand = 'Adidas' and Specification.shoeSize = '39';

-- produkter i varje kategori, right join för att visa kategorier som inte har tillhörande produkter
select Category.categoryName, count(ProductInCategory.categoryId) as productCount from ProductInCategory 
right join Category on Category.id = ProductInCategory.categoryId
group by Category.categoryName, ProductInCategory.categoryId;

-- totalen varje kund handlat för, left join för att visa kunder som inte lagt någon order
select Customer.firstName, Customer.lastName, sum(OrderedProduct.quantity) as productsOrdered, count(distinct CustomerOrder.id) as ordersPlaced, sum(OrderedProduct.quantity * Specification.price) as total from Customer
left join CustomerOrder on CustomerOrder.customerId = Customer.id
left join OrderedProduct on OrderedProduct.orderId = CustomerOrder.id
left join Product on Product.id = OrderedProduct.productId
left join Specification on Specification.id = Product.specId
group by Customer.id;

-- total försäljning i varje stad
-- mjölby har ej sålts för över 1000
select Location.city, sum(OrderedProduct.quantity * Specification.price) as total from Customer
inner join Location on Location.id = Customer.locationId
inner join CustomerOrder on CustomerOrder.customerId = Customer.id
inner join OrderedProduct on OrderedProduct.orderId = CustomerOrder.id
inner join Product on Product.id = OrderedProduct.productId
inner join Specification on Specification.id = Product.specId
group by Location.id
having total > 1000;

-- top 5 sålda produkter
select Product.productName, Specification.brand, sum(OrderedProduct.quantity) as sold from OrderedProduct
inner join Product on Product.id = OrderedProduct.productId
inner join Specification on Specification.id = Product.specId
group by Product.id
order by sold desc 
limit 5;

-- månad med störst försäljning
-- använder distinct för att räkna unika orderid:n
select sum(OrderedProduct.quantity * Specification.price) as totalSales, count(distinct CustomerOrder.id) as totalOrders, date_format(CustomerOrder.dateOfOrder, '%Y-%M') as month
from CustomerOrder
inner join OrderedProduct on OrderedProduct.orderId = CustomerOrder.id
inner join Product on Product.id = OrderedProduct.productId
inner join Specification on Specification.id = Product.specId
group by month
order by totalSales desc limit 1;

select Inventory.quantity from Inventory
inner join Product on Product.id = Inventory.productId
where Product.productName = 'Samba';

select Specification.shoeSize, Inventory.quantity from Specification
left join Product on Product.specId = Specification.id
left join Inventory on Inventory.productId = Product.id
where Product.productName = 'Samba';

SELECT Product.id, Product.productName, Specification.price, Specification.shoeSize, Specification.color, Specification.brand, Category.id, Category.categoryName
from Product
inner join Specification on Specification.id = Product.specId
inner join ProductInCategory on ProductInCategory.productId = Product.id
inner join Category on Category.id = ProductInCategory.categoryId
where Category.id = ProductInCategory.categoryId;

SELECT ShoppingCart.id as shoppingCartId, ShoppingCart.customerId, CartItem.productId, CartItem.quantity
from ShoppingCart 
inner join CartItem on CartItem.cartId = ShoppingCart.id 
inner join Customer on Customer.id = ShoppingCart.customerId;

SELECT OrderedProduct.orderId, OrderedProduct.productId, OrderedProduct.quantity
FROM OrderedProduct
INNER JOIN CustomerOrder ON CustomerOrder.id = OrderedProduct.orderId
INNER JOIN Customer ON Customer.id = CustomerOrder.customerId
WHERE CustomerOrder.customerId = 1;

update Inventory
inner join OrderedProduct on OrderedProduct.productId = Inventory.productId
inner join CustomerOrder on CustomerOrder.id = OrderedProduct.orderId
set Inventory.quantity = Inventory.quantity - (select OrderedProduct.quantity from OrderedProduct where OrderedProduct.orderId = latestOrderId)
where CustomerOrder.id = latestOrderId
and Inventory.productId = OrderedProduct.productId;

update Inventory
inner join OrderedProduct on OrderedProduct.productId = Inventory.productId
set Inventory.quantity = Inventory.quantity - OrderedProduct.quantity
where OrderedProduct.orderId = 1;

select * from ShoppingCart;
select * from CartItem;
select * from Inventory;
select * from OutOfStock;
select * from CartItem inner join ShoppingCart on ShoppingCart.id = CartItem.cartId where ShoppingCart.id = 1;
select * from CustomerOrder;
select * from OrderedProduct;
select * from Product;
select * from Specification;
select * from Customer;

select * from CustomerOrder;
select * from OrderedProduct;
select * from Inventory;
select * from OutOfStock;
select * from CartItem;


-- LÄGG TILL NY PRODUKT TILL PERS KUNDVAGN (productId 7)
-- hur kundvagn ser ut innan:
select Customer.id as CustomerId, Customer.firstName, ShoppingCart.id as CartId, Product.productName, CartItem.productId as ProductId, CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Per';
call AddToCart(1, 1, 7);
-- hur kundvagn ser ut efter:
select Customer.id as CustomerId, Customer.firstName, ShoppingCart.id as CartId, Product.productName, CartItem.productId as ProductId, CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Per';

-- lägg till en till av befintlig vara i Pers kundvagn (productId 1)
call AddToCart(1, 1, 7);
-- hur kundvagn ser ut efter:
select Customer.id as CustomerId, Customer.firstName, ShoppingCart.id as CartId, Product.productName, CartItem.productId as ProductId, CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Per';

-- lägg till vara till Pers kundvagn som redan ligger i OutOfStock (productId 9)
-- här ligger varan:
select * from OutOfStock;
call AddToCart(1, 1, 9);
-- det ser oförändrat ut i kundvagnen:
select Customer.id as CustomerId, Customer.firstName, ShoppingCart.id as CartId, Product.productName, CartItem.productId as ProductId, CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Per';

-- LÄGG BESTÄLLNING PÅ PERS KUNDVAGN
-- Order-historik innan:
select Customer.id as 'CustomerId', Customer.firstName, CustomerOrder.id as 'OrderId', OrderedProduct.productId as 'ProductId', OrderedProduct.quantity as 'Product qty', Product.productName, Inventory.quantity as 'Stock qty' from OrderedProduct
inner join CustomerOrder on CustomerOrder.id = OrderedProduct.orderId
inner join Customer on Customer.id = CustomerOrder.customerId
inner join Product on Product.id = OrderedProduct.productId
inner join Inventory on Inventory.productId = Product.id
where Customer.firstName = 'Per';

call PlaceOrder(1, 1);
-- kundvagnen är nu tom:
select Customer.id as CustomerId, Customer.firstName, ShoppingCart.id as CartId, Product.productName, CartItem.productId as ProductId, CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Per';

-- produkter har flyttat till order-historik, OrderedProduct:
select Customer.id as 'CustomerId', Customer.firstName, CustomerOrder.id as 'OrderId', OrderedProduct.productId as 'ProductId', OrderedProduct.quantity as 'Product qty', Product.productName, Inventory.quantity as 'Stock qty' from OrderedProduct
inner join CustomerOrder on CustomerOrder.id = OrderedProduct.orderId
inner join Customer on Customer.id = CustomerOrder.customerId
inner join Product on Product.id = OrderedProduct.productId
inner join Inventory on Inventory.productId = Product.id
where Customer.firstName = 'Per';

-- ANNA LÄGGER ORDER PÅ BROWN BREEZE SOM TAR SLUT I LAGER EFTER DET (productId 6)
-- Hur order-historik ser ut innan:
select Customer.id as 'CustomerId', Customer.firstName, CustomerOrder.id as 'OrderId', CustomerOrder.dateOfOrder, OrderedProduct.productId as 'ProductId', OrderedProduct.quantity as 'Product qty', Product.productName, Inventory.quantity as 'Stock qty' from OrderedProduct
inner join CustomerOrder on CustomerOrder.id = OrderedProduct.orderId
inner join Customer on Customer.id = CustomerOrder.customerId
inner join Product on Product.id = OrderedProduct.productId
inner join Inventory on Inventory.productId = Product.id
where Customer.firstName = 'Anna';
-- Hur kungvagn ser ut innan:
select Customer.id as CustomerId, Customer.firstName, ShoppingCart.id as CartId, Product.productName, CartItem.productId as ProductId, CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Anna';

call PlaceOrder(2, 2);

-- Hur order-historik ser ut efter:
select Customer.id as 'CustomerId', Customer.firstName, CustomerOrder.id as 'OrderId', CustomerOrder.dateOfOrder, OrderedProduct.productId as 'ProductId', OrderedProduct.quantity as 'Product qty', Product.productName, Inventory.quantity as 'Stock qty' from OrderedProduct
inner join CustomerOrder on CustomerOrder.id = OrderedProduct.orderId
inner join Customer on Customer.id = CustomerOrder.customerId
inner join Product on Product.id = OrderedProduct.productId
inner join Inventory on Inventory.productId = Product.id
where Customer.firstName = 'Anna';

-- produkt har flyttat till outofstock-tabell:
select * from OutOfStock;

-- OLLE LÄGGER ORDER PÅ BROWN BREEZE SOM ÄR SLUT I LAGER (productId 6)
-- Hur kundvagnen ser ut innan i kundvagn:
select Customer.id as 'CustomerId', Customer.firstName, ShoppingCart.id as 'CartId', Product.productName, CartItem.productId as 'ProductId', CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Olle';

-- Hur order-historik ser ut:
select Customer.id as 'CustomerId', Customer.firstName, CustomerOrder.id as 'OrderId', CustomerOrder.dateOfOrder, OrderedProduct.productId as 'ProductId', OrderedProduct.quantity as 'Product qty', Product.productName, Inventory.quantity as 'Stock qty' from OrderedProduct
inner join CustomerOrder on CustomerOrder.id = OrderedProduct.orderId
inner join Customer on Customer.id = CustomerOrder.customerId
inner join Product on Product.id = OrderedProduct.productId
inner join Inventory on Inventory.productId = Product.id
where Customer.firstName = 'Olle';

call PlaceOrder(3, 3);

-- Produkterna ligger kvar i kungvagn:
select Customer.id as 'CustomerId', Customer.firstName, ShoppingCart.id as 'CartId', Product.productName, CartItem.productId as 'ProductId', CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Olle';

-- Ingen ny order:
select Customer.id as 'CustomerId', Customer.firstName, CustomerOrder.id as 'OrderId', CustomerOrder.dateOfOrder, OrderedProduct.productId as 'ProductId', OrderedProduct.quantity as 'Product qty', Product.productName, Inventory.quantity as 'Stock qty' from OrderedProduct
inner join CustomerOrder on CustomerOrder.id = OrderedProduct.orderId
inner join Customer on Customer.id = CustomerOrder.customerId
inner join Product on Product.id = OrderedProduct.productId
inner join Inventory on Inventory.productId = Product.id
where Customer.firstName = 'Olle';

-- OLLE TÖMMER KUNDVAGNEN

call ClearShoppingCart(3);

-- Olles tomma kundvagn:
select Customer.id as 'CustomerId', Customer.firstName, ShoppingCart.id as 'CartId', Product.productName, CartItem.productId as 'ProductId', CartItem.quantity as 'In cart qty', Inventory.quantity as 'Stock qty' from CartItem
inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
inner join Customer on Customer.id = ShoppingCart.customerId
inner join Inventory on Inventory.productId = CartItem.productId
inner join Product on Product.id = CartItem.productId
where Customer.firstName = 'Olle';

delimiter //
create trigger After_insert_customer after insert on Customer
for each row
begin
insert into ShoppingCart(customerId) values (NEW.id);
end//
delimiter ;

delimiter //
create trigger After_Inventory_update after update on Inventory
for each row
begin
	if NEW.quantity = 0
	then
    insert into OutOfStock(productId, soldOutSince) values (OLD.productId, current_timestamp());
    end if;
end //
delimiter ;

delimiter //
create procedure ClearShoppingCart(IN inShoppingCartId int)
begin
delete from CartItem where CartItem.cartId = inShoppingCartId;
end//
delimiter ;

delimiter //
create procedure PlaceOrder(IN inShoppingCartId int, IN inCustomerId int)
begin
	declare latestOrderId int default 0;
    
	declare exit handler for sqlexception
	begin
		rollback;
		resignal set message_text = 'Error while placing order';
	end;
    
	declare exit handler for 1645
    begin
		rollback;
		resignal set message_text = 'Order not completed'; 
    end;

    
    set autocommit = 0;
    start transaction;
    
		if exists (select 1 from CartItem inner join Inventory on Inventory.productId = CartItem.productId where CartItem.cartId = inShoppingCartId and CartItem.quantity > Inventory.quantity)
        then 
        rollback;
        resignal;
		end if;
        
		insert into CustomerOrder(dateOfOrder, customerId) values (current_timestamp, inCustomerId);
		select LAST_INSERT_ID() into latestOrderId;
            
		insert into OrderedProduct(orderId, productId, quantity) select latestOrderId, CartItem.productId, CartItem.quantity from CartItem
		inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
		where ShoppingCart.customerId = inCustomerId;
            
		delete from CartItem where CartItem.cartId = inShoppingCartId;
			
		update Inventory
		inner join OrderedProduct on OrderedProduct.productId = Inventory.productId
		set Inventory.quantity = Inventory.quantity - OrderedProduct.quantity
		where OrderedProduct.orderId = latestOrderId;
	commit;
end//
delimiter ;


delimiter //
create procedure AddToCart(IN inCustomerId int, IN inShoppingCartId int, IN inProductId int)
begin
	declare exit handler for sqlexception
	begin
		rollback;
		resignal set message_text = 'Can not add product to cart; product is out of stock';
	end;
    
    set autocommit = 0;
	start transaction;

		if (select count(*) from OutOfStock where OutOfStock.productId = inProductId > 0)
		then
		rollback;
		resignal;
		end if;

		if exists (
		select * from CartItem 
		inner join ShoppingCart on ShoppingCart.id = CartItem.cartId 
		inner join Product on Product.id = CartItem.productId 
		where ShoppingCart.customerId = inCustomerId 
		and Product.id = inProductId)
		then
			update CartItem
			inner join ShoppingCart on ShoppingCart.id = CartItem.cartId
			set CartItem.quantity = CartItem.quantity + 1 
			where CartItem.cartId = inShoppingCartId 
			and CartItem.productId = inProductId
			and ShoppingCart.customerId = inCustomerId;
		else
			insert into CartItem(cartId, productId, quantity) values (inShoppingCartId, inProductId, 1);
		end if;
 	commit;
end//
delimiter ;