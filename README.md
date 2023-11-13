# Praca Inżynierska


## Instrukcja Uruchomienia

### Zmienne środowiskowe, certyfikat

```
TODO informacja o plikach .env, certyfikacie
```

### Instalacja zależności

Aby zbudować i uruchomić projekt należy zainstalować `Dockera` zgodnie z instrukcjami zawartymi w oficjalnej dokumentacji\
[https://docs.docker.com/](https://docs.docker.com/)\
[Oficjalna dokumentacja dla Linuxa](https://docs.docker.com/engine/install/)\
[Oficjalna instrukcja instalacji (Debian)](https://docs.docker.com/engine/install/debian/)\
[Oficjalna instrukcja instalacji (Ubuntu)](https://docs.docker.com/engine/install/ubuntu/)\
[Oficjalna instrukcja instalacji (Fedora)](https://docs.docker.com/engine/install/fedora/)\
[Oficjalna instrukcja instalacji (CentOS)](https://docs.docker.com/engine/install/centos/)


### Budowanie

Aby zbudować projekt należy wykonać poniższe polecenie:
```bash
docker compose build
```
Po zbudowaniu można sprawdzić, czy powstały wymagane obrazy Dockera wpisując polecenie:
```bash
docker images -a 
```
Wyświetlona lista powinna zawierać poniższe obrazy:
- `rozgrywkitenisa/backend`  
- `rozgrywkitenisa/frontend` 

Przykładowo
```
REPOSITORY                 TAG       IMAGE ID       CREATED         SIZE
rozgrywkitenisa/backend    latest    f418bc2334d5   4 seconds ago   369MB
rozgrywkitenisa/frontend   latest    7ca9fe683613   40 seconds ago  759MB

```

### Uruchamianie
Przed uruchomieniem należy upewnić się, że żaden inny proces nie używa portu 80, lub 443.
Domyślna instalacja KDE Neon i innych dystrybucji opartych na Ubuntu uruchamia apache2, który używa port 80.
Aby go zatrzymać należy wykonać polecenie:
```bash
sudo systemctl stop apache2
```

Aby uruchomić projekt należy wykonać polecenie:
```bash
docker compose up -d
```
Flaga `-d` nie jest wymagana, uruchamia projekt w tle.
