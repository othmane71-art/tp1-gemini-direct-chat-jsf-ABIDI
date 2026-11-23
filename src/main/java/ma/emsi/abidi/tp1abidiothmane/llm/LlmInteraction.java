package ma.emsi.abidi.tp1abidiothmane.llm;

/**
 * Résultat d'un échange avec l'API du LLM.
 *
 * @param questionJson    le JSON de la requête envoyée à l'API (mis en forme)
 * @param reponseJson     le JSON de la réponse reçue de l'API
 * @param reponseExtraite le texte de la réponse, extrait du JSON, à afficher à l'utilisateur
 */
public record LlmInteraction(String questionJson, String reponseJson, String reponseExtraite) {
}