# Tests — API Gemini en accès direct

Environnement : Payara Server, Java 25, modèle `gemini-3.5-flash-lite`.
Chaque test a été fait en mode debug. La clé réelle reste dans la variable d'environnement `GEMINI_KEY`.

## Test 1 — Question simple

- **But** : vérifier que la question arrive au LLM et que la réponse s'affiche.
- **Rôle** : Assistant. **Question** : `stop`.
- **Résultat** : réponse affichée, conversation mise à jour, 55 tokens utilisés (32 en entrée, 23 en sortie).

![Test 1](docs/images/01-chat-assistant-debug.png)

La requête JSON contient `system_instruction` (rôle système) et `contents` avec un message `user`.
La réponse JSON indique `modelVersion: gemini-3.5-flash-lite`.

## Test 2 — Historique de la conversation

- **But** : vérifier que l'historique est renvoyé à chaque requête.
- **Rôle** : Guide touristique. **Questions** : `Capitale de la Romanie?`, puis `et celle du maroc ?`.

![Test 2 — première requête](docs/images/02-historique-guide-touristique.png)

JSON de la **deuxième** requête (réponse du modèle abrégée avec `...`) :

```json
{
  "system_instruction": { "parts": [ { "text": "Your are a travel guide. ..." } ] },
  "contents": [
    { "role": "user",  "parts": [ { "text": "Capitale de la Romanie? " } ] },
    { "role": "model", "parts": [ { "text": "La capitale de la Roumanie est **Bucarest** ! ..." } ] },
    { "role": "user",  "parts": [ { "text": "et celle du maroc ?" } ] }
  ]
}
```

- **Résultat** : Gemini répond **Rabat**. Il a compris que « celle » désigne la capitale, grâce à l'historique.
- Les tokens en entrée passent de 59 à 414 entre les deux requêtes.
- C'est `JsonAdapterPourGemini` qui conserve l'historique, dans son attribut `requeteJson`.

## Test 3 — Clé secrète invalide

- **Modification (non commitée)** : dans `LlmClientPourGemini`, la lecture de `GEMINI_KEY` est remplacée par une clé fausse.

![Test 3 — code modifié](docs/images/03-code-cle-fausse.png)

- **Résultat** :

![Test 3 — erreur](docs/images/04-erreur-cle-invalide.png)

```text
== User:
test
== Serveur:
Erreur : 400 : Bad RequestReponse API : {
  "error": {
    "code": 400,
    "message": "API key not valid. Please pass a valid API key.",
    "status": "INVALID_ARGUMENT",
    "details": [ ... "reason": "API_KEY_INVALID" ... ]
  }
}
```

- **Analyse** : l'API rejette la clé (HTTP 400). `JsonAdapterPourGemini` lance `RequeteException` à la ligne 103, et `Bb.envoyer` (ligne 176) affiche le message.

## Test 4 — Rôle `system` dans `contents`

- **Modification (non commitée)** : dans `creerRequeteJson`, `"user"` est remplacé par `"system"`.

![Test 4 — code modifié](docs/images/05-code-role-system.png)

- **Résultat** :

![Test 4 — erreur](docs/images/06-erreur-role-system.png)

```text
== User:
test
== Serveur:
Erreur : 400 : Bad RequestReponse API : {
  "error": {
    "code": 400,
    "message": "Role 'system' is not supported. Please use a valid role: MODEL, USER.",
    "status": "INVALID_ARGUMENT"
  }
}
```

- **Analyse** : `contents` n'accepte que `user` et `model`. Le rôle système se place dans `system_instruction`.

## Retour au code sain

Les deux modifications n'ont pas été commitées. Le retour au dernier commit `ecce702` s'est fait avec :

```bat
git restore .
git status
```

Résultat de `git status` : 
```
git status   
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

## Récapitulatif

| N° | Test | Statut HTTP | Message principal |
|---|---|---|---|
| 1 | Question simple | 200 | Réponse du LLM |
| 2 | Historique | 200 | Réponse « Rabat » avec contexte |
| 3 | Clé fausse | 400 | `API key not valid` |
| 4 | Rôle `system` | 400 | `Role 'system' is not supported` |
