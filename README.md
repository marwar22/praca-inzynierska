# Praca Inżynierska

## Instrukcja Uruchomienia

### Zmienne środowiskowe, certyfikat

Należy utworzyć pliki zawierające zmienne środowiskowe (.env)

W `/`
```bash
cp .env.example .env
# należy ustawić wartości zmiennych środowiskowych w pliku .env
```

W `frontend/`
```bash
cd frontend
cp .env.example .env
# należy ustawić wartości zmiennych środowiskowych w pliku .env
```

W przypadku uruchamiania projektu w środowisku testowym należy ręcznie wygenerować `certyfikat SSL`
```bash
./generate_cert.sh

# Zawartość pliku generate_cert.sh:
#
# mkdir -p data/certbot/letsencrypt/live/rozgrywkitenisa.pl
# openssl req -nodes -newkey rsa:2048 -keyout data/certbot/letsencrypt/live/rozgrywkitenisa.pl/privkey.pem -x509 -out data/certbot/letsencrypt/live/rozgrywkitenisa.pl/fullchain.pem
```


### Instalacja zależności

Aby zbudować i uruchomić projekt należy zainstalować `Docker` zgodnie z instrukcjami zawartymi w oficjalnej dokumentacji\
[https://docs.docker.com/](https://docs.docker.com/)\
[Oficjalna dokumentacja dla Linuxa](https://docs.docker.com/engine/install/)\
[Oficjalna instrukcja instalacji (Debian)](https://docs.docker.com/engine/install/debian/)\
[Oficjalna instrukcja instalacji (Ubuntu)](https://docs.docker.com/engine/install/ubuntu/)\
[Oficjalna instrukcja instalacji (Fedora)](https://docs.docker.com/engine/install/fedora/)\
[Oficjalna instrukcja instalacji (CentOS)](https://docs.docker.com/engine/install/centos/)

Użytkownik musi należeć do grupy `docker`

```bash
sudo usermod -aG docker nazwa_uzytkownika
```

### Budowanie i Uruchamianie wersja TL;DR

```bash
# środowisko produkcyjne
docker compose up -d --build
# środowisko lokalne
docker compose -f docker-compose-localhost.yml up -d --build
```

### Budowanie

Aby zbudować projekt należy wykonać poniższe polecenie:

```bash
# środowisko produkcyjne
docker compose build
# środowisko lokalne
docker compose -f docker-compose-localhost.yml build
```

Po zbudowaniu można sprawdzić, czy powstały wymagane obrazy Dockera wpisując polecenie:

```bash
docker images -a
```

Wyświetlona lista powinna zawierać poniższe obrazy:

-   `rozgrywkitenisa/backend`
-   `rozgrywkitenisa/frontend`

Przykładowo (`IMAGE ID` będzie inne)

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
# środowisko produkcyjne
docker compose up -d
# środowisko lokalne
docker compose -f docker-compose-localhost.yml up -d
```

Flaga `-d` nie jest wymagana, uruchamia projekt w tle.


### Wyłączanie

```bash
# środowisko produkcyjne, lub lokalne
docker compose down
```
