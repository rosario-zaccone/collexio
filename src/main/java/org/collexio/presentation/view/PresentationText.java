package org.collexio.presentation.view;

import java.util.Map;

public final class PresentationText {
    public enum Language {
        ENGLISH,
        ITALIAN
    }

    private static Language language = Language.ENGLISH;

    private static final Map<String, String> ITALIAN = Map.ofEntries(
            Map.entry("View", "Vista"),
            Map.entry("Language", "Lingua"),
            Map.entry("English", "Inglese"),
            Map.entry("Italian", "Italiano"),
            Map.entry("Collections", "Collezioni"),
            Map.entry("Specs", "Specifiche oggetto"),
            Map.entry("Item specification", "Specifica oggetto"),
            Map.entry("Item specifications", "Specifiche oggetto"),
            Map.entry("Item", "Oggetto"),
            Map.entry("Items", "Oggetti"),
            Map.entry("Transactions", "Transazioni"),
            Map.entry("+ Add", "+ Aggiungi"),
            Map.entry("Print", "Stampa"),
            Map.entry("Insert", "Inserisci"),
            Map.entry("Update", "Aggiorna"),
            Map.entry("Delete", "Elimina"),
            Map.entry("Cancel", "Annulla"),
            Map.entry("Scrape price", "Stima prezzo"),
            Map.entry("Price scraped", "Prezzo stimato"),
            Map.entry("Id", "Id"),
            Map.entry("ID:", "ID:"),
            Map.entry("Name", "Nome"),
            Map.entry("Name:", "Nome:"),
            Map.entry("Type", "Tipo"),
            Map.entry("Type:", "Tipo:"),
            Map.entry("Description", "Descrizione"),
            Map.entry("Description:", "Descrizione:"),
            Map.entry("Photo", "Foto"),
            Map.entry("Photo:", "Foto:"),
            Map.entry("Status", "Stato"),
            Map.entry("Status:", "Stato:"),
            Map.entry("Date", "Data"),
            Map.entry("Total", "Totale"),
            Map.entry("Amount", "Importo"),
            Map.entry("Amount (€):", "Importo (€):"),
            Map.entry("Item Id", "Id oggetto"),
            Map.entry("Item id:", "Id oggetto:"),
            Map.entry("Item specification id:", "Id specifica oggetto:"),
            Map.entry("Transaction id:", "Id transazione:"),
            Map.entry("Collection id", "Id collezione"),
            Map.entry("Filter by Collection ID:", "Filtra per id collezione:"),
            Map.entry("Filter by Item ID:", "Filtra per id oggetto:"),
            Map.entry("Insert collection", "Inserisci collezione"),
            Map.entry("Update collection", "Aggiorna collezione"),
            Map.entry("Insert item", "Inserisci oggetto"),
            Map.entry("Update item", "Aggiorna oggetto"),
            Map.entry("Insert item specification", "Inserisci specifica oggetto"),
            Map.entry("Update item specification", "Aggiorna specifica oggetto"),
            Map.entry("Insert transaction", "Inserisci transazione"),
            Map.entry("Update transaction", "Aggiorna transazione"),
            Map.entry("Generate description with AI", "Genera descrizione con IA"),
            Map.entry("Collection Id (Leave empty if the item isn't part of a collection):",
                    "Id collezione (lascia vuoto se l'oggetto non appartiene a una collezione):"),
            Map.entry("Name (latin name for plants):", "Nome (nome latino per le piante):"),
            Map.entry("Photo date (YYYY-MM-DD):", "Data foto (AAAA-MM-GG):"),
            Map.entry("Date (YYYY-MM-DD):", "Data (AAAA-MM-GG):"),
            Map.entry("No collection", "Nessuna collezione"),
            Map.entry("Not available", "Non disponibile"),
            Map.entry("Income", "Entrata"),
            Map.entry("Expense", "Uscita"),
            Map.entry("Bad", "Scarso"),
            Map.entry("Average", "Medio"),
            Map.entry("Good", "Buono"),
            Map.entry("Plant", "Pianta"),
            Map.entry("Tech Item", "Oggetto tech"),
            Map.entry("Book", "Libro"),
            Map.entry("Are you sure?", "Sei sicuro?"),
            Map.entry("Delete confirm", "Conferma eliminazione"),
            Map.entry("Error", "Errore"),
            Map.entry("Database Error", "Errore database"),
            Map.entry("Startup Error", "Errore avvio"),
            Map.entry("Startup failed: ", "Avvio fallito: "),
            Map.entry("Error refreshing data: ", "Errore durante l'aggiornamento dati: "),
            Map.entry("Sorry, no price available", "Spiacente, nessun prezzo disponibile"),
            Map.entry("Browse...", "Sfoglia..."),
            Map.entry("Item Collection inserted", "Collezione inserita"),
            Map.entry("Item Collection updated", "Collezione aggiornata"),
            Map.entry("Item spec inserted", "Specifica oggetto inserita"),
            Map.entry("Item spec updated", "Specifica oggetto aggiornata"),
            Map.entry("Item inserted", "Oggetto inserito"),
            Map.entry("Item updated", "Oggetto aggiornato"),
            Map.entry("Transaction inserted", "Transazione inserita"),
            Map.entry("Transaction updated", "Transazione aggiornata"),
            Map.entry("Input Error: ", "Errore input: "),
            Map.entry("Database Error: ", "Errore database: "),
            Map.entry("IO Error: ", "Errore I/O: "),
            Map.entry("Error: ", "Errore: "),
            Map.entry("Printer Error: ", "Errore stampante: "),
            Map.entry("Error during description generation: ", "Errore durante la generazione della descrizione: "),
            Map.entry("Please wait", "Attendere"),
            Map.entry("Scraping price, please wait...", "Stima del prezzo in corso, attendere..."),
            Map.entry("Generating description, please wait...", "Generazione descrizione in corso, attendere..."),
            Map.entry("Initial Setup", "Configurazione iniziale"),
            Map.entry("Gemini API key (optional):", "Chiave API Gemini (opzionale):"),
            Map.entry("Used only to generate item descriptions with AI.",
                    "Usata solo per generare descrizioni degli oggetti con l'IA."),
            Map.entry("Continue without Gemini API key", "Continua senza chiave API Gemini"),
            Map.entry("Contact email:", "Email di contatto:"),
            Map.entry("Required for scraping requests.", "Richiesta per le operazioni di scraping."),
            Map.entry("Contact email is required to run Collexio.",
                    "L'email di contatto è necessaria per avviare Collexio."),
            Map.entry("Contact email is required.", "L'email di contatto è obbligatoria."),
            Map.entry("Startup failed:\n", "Avvio fallito:\n")
    );

    private PresentationText() {}

    public static Language getLanguage() {
        return language;
    }

    public static void setLanguage(Language nextLanguage) {
        language = nextLanguage;
    }

    public static String text(String value) {
        if (language == Language.ITALIAN) {
            return ITALIAN.getOrDefault(value, value);
        }
        return value;
    }
}
