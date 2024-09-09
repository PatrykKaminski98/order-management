# Order Management REST API

## Opis

Ten projekt został stworzony w ramach zadania rekrutacyjnego na stanowisko **Java Developer**. Serwis REST obsługuje procesy związane z zarządzaniem zamówieniami, a jego główne funkcjonalności obejmują:

- Tworzenie zamówień
- Potwierdzanie zamówień
- Anulowanie zamówień
- Zakańczanie zamówień
- Listowanie produktów
- Tworzenie nowych użytkowników

Projekt został zbudowany w oparciu o **Spring Boot** i posiada prosty mechanizm uwierzytelniana użytkowników. Schemat bazy danych jest zarządzany za pomocą **Liquibase**, które automatycznie tworzy i wypełnia bazę danych początkowymi danymi.

## Endpoints

### 1. Pobieranie szczegółów zamówienia

**GET** `/api/order/{orderKey}`  
Zwraca szczegóły zamówienia, w tym listę produktów w zamówieniu.

- **Parametry**:
    - `orderKey`: Klucz zamówienia
- **Odpowiedź**:
    - **200 OK**: Zwraca szczegóły zamówienia
    - **404 Not Found**: Zamówienie o podanym `orderKey` nie zostało znalezione

### 2. Tworzenie nowego zamówienia

**POST** `/api/order/create`  
Tworzy nowe zamówienie na podstawie danych wejściowych.

- **Body**:
    - JSON z danymi zamówienia:
      ```json
      {
        "customerKey": "string",
        "orderItems": [
          {
            "productIdentifier": "string",
            "quantity": "integer"
          }
        ]
      }
      ```
    - `customerKey`: Klucz klienta
    - `orderItems`: Lista przedmiotów zamówienia, gdzie każdy przedmiot zawiera:
        - `productIdentifier`: Identyfikator produktu z tabeli `Product`
        - `quantity`: Ilość produktu
- **Odpowiedź**:
    - **200 OK**: Zwraca utworzone zamówienie wraz z wygenerowanym `orderKey`
      ```json
      {
        "orderKey": "string",
        "customerKey": "string",
        "status": "CREATED",
        "orderItems": [
          {
            "productIdentifier": "string",
            "quantity": "integer"
          }
        ]
      }
      ```
    - **400 Bad Request**: Niepoprawne dane wejściowe

### 3. Potwierdzenie zamówienia

**PUT** `/api/order/confirm/{orderKey}`  
Potwierdza zamówienie.

- **Parametry**:
    - `orderKey`: Klucz zamówienia
- **Odpowiedź**:
    - **200 OK**: Zamówienie zostało potwierdzone
    - **404 Not Found**: Zamówienie o podanym `orderKey` nie zostało znalezione
    - **409 Conflict**: Zamówienie zostało już wcześniej anulowane lub zakończone

### 4. Anulowanie zamówienia

**PUT** `/api/order/cancel/{orderKey}`  
Anuluje zamówienie.

- **Parametry**:
    - `orderKey`: Klucz zamówienia
- **Odpowiedź**:
    - **200 OK**: Zamówienie zostało anulowane
    - **404 Not Found**: Zamówienie o podanym `orderKey` nie zostało znalezione
    - **409 Conflict**: Zamówienie zostało już wcześniej potwierdzone lub zakończone

### 5. Zakończenie zamówienia

**PUT** `/api/order/complete/{orderKey}`  
Zamyka zamówienie.

- **Parametry**:
    - `orderKey`: Klucz zamówienia
- **Odpowiedź**:
    - **200 OK**: Zamówienie zostało zakończone
    - **404 Not Found**: Zamówienie o podanym `orderKey` nie zostało znalezione
    - **409 Conflict**: Zamówienie zostało już wcześniej anulowane lub zakończone

### 6. Listowanie wszystkich produktów

**GET** `/api/product`  
Zwraca listę wszystkich produktów dostępnych w systemie.

- **Odpowiedź**:
    - **200 OK**: Zwraca listę produktów
      ```json
      [
        {
          "productIdentifier": "PF1835",
          "name": "Smartphone Samsung",
          "producer": "Samsung",
          "price": 1200
        },
        {
          "productIdentifier": "HS1836",
          "name": "Smartphone Apple",
          "producer": "Apple",
          "price": 3500
        }
      ]
      ```

### 7. Rejestracja nowego użytkownika

**POST** `/register`  
Rejestruje nowego użytkownika w systemie.

- **Body**:
  ```json
  {
    "username": "string",
    "password": "string",
    "role": "string"
  }
  ```
- **Odpowiedź**:
    - **201 Created**: Nowy użytkownik został utworzony
    - **400 Bad Request**: Niepoprawne dane wejściowe

## Obsługa błędów

- **400 Bad Request**: Zostanie zwrócone, gdy dane wejściowe są niepoprawne (np. brak wymaganych pól lub nieprawidłowy format danych).
- **404 Not Found**: Zasób o podanym identyfikatorze nie istnieje.
- **409 Conflict**: Wystąpił konflikt, np. próba potwierdzenia zamówienia, które zostało już anulowane.
- **500 Internal Server Error**: Niespodziewany błąd na serwerze.

## Bezpieczeństwo

- Dostęp do endpointów zarządzania zamówieniami (tworzenie, potwierdzanie, anulowanie, zakańczanie) jest ograniczony tylko do **zalogowanych** użytkowników.
- Rejestracja nowych użytkowników jest możliwa za pomocą endpointu `/register`.
- Aplikacja ma domyślnie dwóch zarejestrowanych użytkowników:

    - **admin**:  
      Login: `admin`  
      Hasło: `P@ssw0rd`

    - **user**:  
      Login: `user`  
      Hasło: `Password`

## Baza danych

- Zarządzanie schematem bazy danych odbywa się za pomocą **Liquibase**.
- Liquibase automatycznie tworzy tabele i wypełnia je początkowymi danymi, które ułatwiają testowanie i korzystanie z API.
- Po uruchomieniu aplikacji, Liquibase wykonuje **changeset**, który tworzy wszystkie potrzebne struktury i dane.

## Uruchamianie aplikacji

Aby uruchomić aplikację, wykonaj poniższe kroki:

1. **Zbuduj projekt**:

    ```bash
    mvn clean install
    ```

2. **Uruchom aplikację**:

    ```bash
    mvn spring-boot:run
    ```

3. **Dostęp do API**:  
   Aplikacja będzie dostępna pod adresem: `http://localhost:8080`.

## Technologie

- **Java 17**
- **Spring Boot**
- **Maven**
- **Liquibase**
- **MySQL**
- **MapStruct**

## Autor
### Patryk Kamiński

Aplikacja została stworzona w ramach zadania rekrutacyjnego na stanowisko **Java Developer**.
