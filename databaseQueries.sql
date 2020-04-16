create database ecommerce;
drop database ecommerce; 

use ecommerce;
select * from user;
select * from user_role;
select * from role; 
select * from address; 
select * from admin; 
select * from seller; 
select * from customer;  
select * from product; 
select * from product_review;
select * from product_variation;
select * from category;

select * from category_metadata_field;
select * from category_metadata_field_values;
select * from cart;
select * from order_product;
select * from order_status;
select * from orders;
desc product_variation;
desc user_role;
desc product_review;
desc cart;
desc orders;
desc order_product;
desc order_status;
desc category_metadata_field;
desc category_metadata_field_values;
select * from verification_token;



select * from oauth_access_token;
select * from oauth_refresh_token;




delete from user_role where user_id=1 and role_id=3;
delete from product where id=35;
delete from category where id=21;
delete from category_metadata_field_values where category_id=16;