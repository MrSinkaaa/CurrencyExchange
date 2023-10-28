# CurrencyExchange

REST API to describe currencies and exchange rates Allows you to view and edit a list of currencies and exchange rates,
and transfer from one currency to another.

The project doesnt have a web interface.

## Technologies

- Maven
- Servlets
- REST API
- SQLite
- Tomcat 9
- Deploy in VPS (Ubuntu 20.04)

  ### EndPoins
  
* GET
  * <span style="color:white">/currencies</span> – get all currencies
  * <span style="color:white">/currency/</span>EUR – get selected currency
  * <span style="color:white">/exchangeRates</span> – get all exchange rates 
  * <span style="color:white">/exchangeRate/</span>USDRUB – get selected exchange rate
  * <span style="color:white">/exchange</span>?from=USD&to=EUR&amount=10 – convert from one currency to another
  

* POST
  * add new currency to database
    * <span style="color:white">/currencies</span>?name=US Dollar&code=USD&sign=$
  * add new exchange rate to the database
    * <span style="color:white">/exchangeRates</span>?baseCurrencyCode=USD&targetCurrencyCode=RUB&rate=89.5

  
* PATCH
  * <span style="color:white">/exchangeRate/</span>USDRUB?rate=70 – update existing exchange rate in database
 

  link: http://162.19.230.173:8080/CurrencyExchange/
