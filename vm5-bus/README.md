# VM5 : vm5-bus (bus de messages RabbitMQ)

- Réseau vSphere : ECOPT - SIRIUS - LAN APP (VLAN 20)
- IP : 10.10.20.30/24, passerelle 10.10.20.1
- OS : Ubuntu Server 24.04 LTS
- Technologie : RabbitMQ 3.12 (protocole AMQP, port 5672)
- Interface de gestion : port 15672

## Fichiers du dossier

- `netplan.yaml` : configuration réseau de la VM
- `definitions.json` : files, exchange et liens RabbitMQ

## Files et exchange

- `q.recherche` : demandes de recherche pour les paniers
- `q.resultats` : résultats des workers
- `q.panier.optimise` : paniers optimisés prêts pour l'envoi du mail
- `q.recherche.unitaire` : demandes de recherche unitaire
- `q.reponses.unitaire` : réponses aux recherches unitaires
- `q.analytique` : copie des événements pour le pipeline analytique
- `ex.evenements` (fanout) : copie les événements vers `q.analytique`

## Recherche unitaire (RPC)

Le MS publie dans `q.recherche.unitaire` avec `reply_to` et `correlation_id`.
Le worker répond dans la file `reply_to` avec le même `correlation_id`.

## Restaurer la configuration

    rabbitmqadmin -u UTILISATEUR -p MOT_DE_PASSE import definitions.json
