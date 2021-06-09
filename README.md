# Getting Started

Progetto di Esame per Programmazione Concorrente e Distribuita

## Team Members 

- Ferrario Andrea, 
    Matricola: 740485

- Loschiavo Christian,
    Matricola: 739894

## Folder Structure

Lo spazio di lavoro contiene:

### Entry points

- `Server.java`: Entry point del Server (Pubblicatore)
- `FruitoreNotizie.java`: Entry point del Client

### Buffers
- `BufferNotizie.java` : Area di memoria condivisa, in cui viene gestita la concorrenza, che memorizza le notizie prodotte dai thread ProduttoreNotizia
- `BufferClientInfo.java` : Area di memoria condivisa, in cui viene gestita la concorrenza, che memorizza le informazioni dei client connessi

### Communication Objects
- `ClientRequest.java` : Oggetto richiesta utilizzato dal client per aggiungere una tipologia di notizia a quelle che si desidererebbe ricevere o rimuovere.
- `ServerResponse.java` : Oggetto risposta utilizzato dal server per inviare le notizie prodotte ai FruitoreNotizie connessi (Clients)

### Other files
- `Notizia.java` : Oggetto che contiene il contenuto e tipologia della notizia
- `Pubblicatore.java` : Programma (Thread) che invia ogni 5 secondi le notizie presenti nel BufferNotizie ad ogni client connesso e contiene i Thread ProduttoreNotizie responsabili per la generazione di notizie

## Tutorial
Istruzioni per la compilazione ed esecuzione del programma.  
Il programma è stato testato su Windows & Linux Ubuntu sotto una rete LAN. (Sia sulla stessa macchina che su macchine diverse)  
Il programma è stato testato utlizzando `OpenJDK 11.0.11`
    
### Compilazione
- Linux
1. Aprire un'istanza del Terminale
2. Navigare fino alla directory del progetto (Ferrario-Loschiavo)
3. Eseguire il seguente comando: ``` javac *.java ```

- Windows
1. Aprire un'istanza della PowerShell
2. Navigare fino alla directory del progetto (Ferrario-Loschiavo)
3. Eseguire il seguente comando: ``` javac *.java ```


### Esecuzione 
- Linux & Windows
    - Server: 
    1. Aprire un'istanza del Terminale (Linux) | Powershell (Windows)
    2. Navigare fino alla directory del progetto (Ferrario-Loschiavo)
    3. Eseguire il comando ``` java Server <PORTA> ``` (e.g.: ``` java Server 1234 ```) per avviare il server

    **N.B** ``` <PORTA> ``` La porta deve essere inclusa dalla 1024 a 65535 e deve essere uguale a quella utilizzata dal client

    
    - Client:
    1. Aprire un'istanza del Terminale (Linux) | Powershell (Windows) per ogni singolo client
    2. Navigare fino alla directory del progetto (Ferrario-Loschiavo)
    3. Esegurie il comando ``` java FruitoreNotizie <Indirizzo IPv4> <PORT> ``` (e.g.: ``` java FruitoreNotizie 127.0.0.1 1234 ```) per avviare e far connettere il client al server e iniziare a ricevere notizie

    **N.B.** ``` <PORTA> ``` La porta deve essere inclusa dalla 1024 a 65535 e deve essere uguale a quella utilizzata dal server
        
## Come ottenere l'indirizzo IPv4 del server
**Se si esegue su macchine diverse: **
- Linux
    1. Esegurire il comando ``` ifconfig ``` sul terminale e cercare l'IPv4 che inizia con 192.168.X.X

- Windows
    1. Esegurire il comando ``` ipconfig ``` sulla PowerShell e cercare l'IPv4 che inizia con 192.168.X.X

**Se si esegue client e server sulla stessa macchina: **
    - ``` <Indirizzo IPv4> = 127.0.0.1 ```
    
    



            
         
                                


