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

update address set created_by="seller.seller@tothenew.com", modified_by="seller.seller@tothenew.com" where id=108;
delete from user_role where user_id=1 and role_id=3;
delete from product where id=35;
delete from category where id=115;
delete from category_metadata_field_values where category_id=89 and category_metadata_field_id=82;
update category_metadata_field set is_deleted=false where id>0;
delete from category_metadata_field where id=118;
update category_metadata_field_values set is_deleted=false where category_id>0;
update category set is_deleted=true where id=116;


select * from category_metadata_field f 
inner join category_metadata_field_values v 
on f.id = v.category_metadata_field_id 
where 
v.category_id = 86 and 
v.is_deleted=false and
f.is_deleted=false;

update product set is_deleted=false where id=95;



