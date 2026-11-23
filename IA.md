# Utilisation de l'IA

## 🤖 Outil utilisé

J'ai utilisé **Perplexity** comme assistant, pas comme générateur de projet.

## 🧭 Rôle de l'assistant

| Étape du TP | Aide apportée |
|---|---|
| Choix du modèle | Comparaison des modèles listés dans la documentation, pour garder un accès gratuit : `gemini-3.5-flash-lite` |
| Backing bean et page | Aide pour adapter `Bb.java` et pour placer le panel debug et le toggleSwitch dans `index.xhtml` |
| Tests d'erreur | Aide pour comprendre les messages d'erreur de l'API et pour revenir en arrière avec Git |
| Documentation | Aide pour rédiger le README et le rapport de tests |

## 🔧 Un problème résolu avec l'IA

### Problème rencontré

Après avoir modifié `Bb.java`, la méthode `envoyer()` contenait une accolade `}` en trop après le bloc `catch`.
Cette accolade fermait la méthode trop tôt. Les lignes `afficherConversation();` et `return null;`
se retrouvaient hors de toute méthode, ce qui provoquait une erreur de compilation.

### Raison de l'utilisation de l'IA

J'avais fait plusieurs modifications à la main dans un fichier long (imports, injection, propriétés
du mode debug, méthode `envoyer()`), et je ne voyais pas où était l'erreur de structure.
Je voulais une vérification complète du fichier, qui respecte le code donné par le professeur.

### Prompt utilisé

```text
verifier si tout est bon dans le code Bb.java, sinon corriger la syntaxe code.

[partie de code de Bb.java collé à la suite]
```

### Réponse de l'IA

Extrait pertinent :

> Il y avait une erreur de structure. Dans `envoyer()`, tu as une accolade `}` en trop après le `catch`.
> Elle ferme la méthode trop tôt et laisse les lignes suivantes (`afficherConversation();`, `return null;`, `}`)
> en dehors de toute méthode. Les lignes vides et le commentaire « Traite la question... » sont des restes
> de l'ancien code.

L'assistant m'a ensuite donné le fichier `Bb.java` complet et corrigé, avec les imports remis en ordre.

### Ce que j'ai appris

- Une accolade mal placée ne provoque pas toujours une erreur claire : le compilateur signale
  des erreurs plus loin, sur des lignes qui semblent correctes.
- Quand on remplace un morceau de code, il faut aussi supprimer les restes de l'ancien traitement.
- Dans `envoyer()`, le `try/catch` entoure seulement l'appel au LLM. L'historique est affiché
  après, et la méthode retourne `null` pour rester sur la page.
- Il faut relire le code proposé et le comparer à celui du professeur avant de le remplacer.

## ✅ Ce que j'ai vérifié moi-même

- J'ai testé l'application : question simple, historique sur deux questions, et deux erreurs volontaires.
- J'ai comparé les réponses de l'assistant avec le code et avec les messages réels de l'API.
- J'ai réalisé moi-même les commits, les push et le retour en arrière avec Git.
