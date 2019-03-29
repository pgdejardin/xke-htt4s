# XKE Http4s

This is the project used for the 04/2019 XKE at Xebia France.

## Objective

It is used for a livecoding showing how to use functional programming on a web application.

I want to create from scratch a REST Api with Http4s. 

## Technical

- Scala
- Http4s
- Cats
- Doobie
 
## Information

Routes :
1. [GET]  /books
2. [GET]  /books/{id}
3. [POST] /books
4. [DELETE]  /books/{id}

### TODO before LiveCoding
1. Add author and books in initData

### LiveCoding implementation :
  
1. Add price to book
2. Create Cart Pojo
3. Implements Discount Domain
  - case sum(price) > 0 && <= 20  => discount = 10%
  - case sum(price) > 20 && <= 50 => discount = - 10 €
  - case sum(price) > 50          => discount = - 12 per 100
4. Add endpoint
