package com.adacore.adaintellij.lsp;

import org.eclipse.lsp4j.FailureHandlingKind;

import java.util.Collections;
import java.util.List;

/**
 * Capability settings for Ada-IntelliJ's integrated LSP client.
 * <p>
 * The class structure directly reflects client capability
 * definitions in the LSP protocol.
 */
final class ClientCapabilities {

	private ClientCapabilities() {}

	/**
	 * Workspace capabilities.
	 */
	static final class Workspace {

		static final boolean APPLY_EDIT = false;
		static final boolean WORKSPACE_FOLDERS = false;
		static final boolean CONFIGURATION = false;

		private Workspace() {}

		static final class WorkspaceEdit {

			static final boolean DOCUMENT_CHANGES = false;
			static final List<String> RESOURCE_OPERATIONS = Collections.emptyList();
			static final String FAILURE_HANDLING = FailureHandlingKind.Abort;

			private WorkspaceEdit() {}

		}

		static final class DidChangeConfiguration {

			static final boolean DYNAMIC_REGISTRATION = false;

			private DidChangeConfiguration() {}

		}

		static final class DidChangeWatchedFiles {

			static final boolean DYNAMIC_REGISTRATION = false;

			private DidChangeWatchedFiles() {}

		}

		static final class Symbol {

			static final boolean DYNAMIC_REGISTRATION = false;

			private Symbol() {}

			static final class SymbolKind {

				static final List<org.eclipse.lsp4j.SymbolKind> VALUE_SET =
					Collections.emptyList();

				private SymbolKind() {}

			}

		}

		static final class ExecuteCommand {

			static final boolean DYNAMIC_REGISTRATION = false;

			private ExecuteCommand() {}

		}

	}

	/**
	 * Text document capabilities.
	 */
	static final class TextDocument {

		private TextDocument() {}

		static final class Synchronization {

			static final boolean DYNAMIC_REGISTRATION = false;
			static final boolean WILL_SAVE = true;
			static final boolean WILL_SAVE_WAIT_UNTIL = true;
			static final boolean DID_SAVE = true;

			private Synchronization() {}

		}

		static final class Completion {

			static final boolean DYNAMIC_REGISTRATION = false;
			static final boolean CONTEXT_SUPPORT = false;

			private Completion() {}

			static final class CompletionItem {

				static final boolean SNIPPET_SUPPORT = false;
				static final boolean COMMIT_CHARACTERS_SUPPORT = false;
				static final List<String> DOCUMENTATION_FORMAT = Collections.emptyList();
				static final boolean DEPRECATED_SUPPORT = false;
				static final boolean PRESELECT_SUPPORT = false;

				private CompletionItem() {}

			}

			static final class CompletionItemKind {

				static final List<org.eclipse.lsp4j.CompletionItemKind> VALUE_SET =
					Collections.emptyList();

				private CompletionItemKind() {}

			}

		}

		static final class Hover {

			static final boolean DYNAMIC_REGISTRATION = false;
			static final List<String> CONTENT_FORMAT = Collections.emptyList();

			private Hover() {}

		}

		static final class SignatureHelp {

			static final boolean DYNAMIC_REGISTRATION = false;

			private SignatureHelp() {}

			static final class SignatureInformation {

				static final List<String> DOCUMENTATION_FORMAT = Collections.emptyList();

				private SignatureInformation() {}

			}

		}

		static final class References {

			static final boolean DYNAMIC_REGISTRATION = false;

			private References() {}

		}

		static final class DocumentHighlight {

			static final boolean DYNAMIC_REGISTRATION = false;

			private DocumentHighlight() {}

		}

		static final class DocumentSymbol {

			static final boolean DYNAMIC_REGISTRATION = false;
			static final boolean HIERARCHICAL_DOCUMENT_SYMBOL_SUPPORT = false;

			private DocumentSymbol() {}

			static final class SymbolKind {

				static final List<org.eclipse.lsp4j.SymbolKind> VALUE_SET =
					Collections.emptyList();

				private SymbolKind() {}

			}

		}

		static final class Formatting {

			static final boolean DYNAMIC_REGISTRATION = false;

			private Formatting() {}

		}

		static final class RangeFormatting {

			static final boolean DYNAMIC_REGISTRATION = false;

			private RangeFormatting() {}

		}

		static final class OnTypeFormatting {

			static final boolean DYNAMIC_REGISTRATION = false;

			private OnTypeFormatting() {}

		}

		static final class Definition {

			static final boolean DYNAMIC_REGISTRATION = false;

			private Definition() {}

		}

		static final class TypeDefinition {

			static final boolean DYNAMIC_REGISTRATION = false;

			private TypeDefinition() {}

		}

		static final class Implementation {

			static final boolean DYNAMIC_REGISTRATION = false;

			private Implementation() {}

		}

		static final class CodeAction {

			static final boolean DYNAMIC_REGISTRATION = false;

			private CodeAction() {}

			static final class CodeActionLiteralSupport {

				private CodeActionLiteralSupport() {}

				static final class CodeActionKind {

					static final List<String> VALUE_SET = Collections.emptyList();

					private CodeActionKind() {}

				}

			}

		}

		static final class CodeLens {

			static final boolean DYNAMIC_REGISTRATION = false;

			private CodeLens() {}

		}

		static final class DocumentLink {

			static final boolean DYNAMIC_REGISTRATION = false;

			private DocumentLink() {}

		}

		static final class ColorProvider {

			static final boolean DYNAMIC_REGISTRATION = false;

			private ColorProvider() {}

		}

		static final class Rename {

			static final boolean DYNAMIC_REGISTRATION = false;
			static final boolean PREPARE_SUPPORT = false;

			private Rename() {}

		}

		static final class PublishDiagnostics {

			static final boolean RELATED_INFORMATION = false;

			private PublishDiagnostics() {}

		}

		static final class FoldingRange {

			static final boolean DYNAMIC_REGISTRATION = false;
			static final int RANGE_LIMIT = 0;
			static final boolean LINE_FOLDING_ONLY = false;

			private FoldingRange() {}

		}

	}

}
