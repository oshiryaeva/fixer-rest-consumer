# Sample Spring Boot REST consuming service 
Fetches currency rates through Fixer.io API in a scheduled task. 

Rates are stored in a PostgreSQL database. 

REST endpoints reference:

`/latest` : for getting all latest EUR rates

`/date/{date}` : (in any format) for getting all rates for this date

`/date/{date}/{symbol}` : (date in any format) for getting {symbol} rate for this date

`/symbol/{symbol}` : for getting {symbol} latest rate. See available symbols at fixer.io docs

`{symbol}/{amount}` : to convert the entered amount of {symbol} currency with the latest rate

`/anything/else` to get this app down (no exception handling implemented yet)
