import 'construct-style-sheets-polyfill';
import { DomModule } from "@polymer/polymer/lib/elements/dom-module";
import { stylesFromTemplate } from "@polymer/polymer/lib/utils/style-gather";
import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';

const createLinkReferences = (css, target) => {
  // Unresolved urls are written as '@import url(text);' to the css
  // [0] is the full match
  // [1] matches the media query
  // [2] matches the url
  const importMatcher = /(?:@media\s(.+?))?(?:\s{)?\@import\surl\((.+?)\);(?:})?/g;
  
  var match;
  var styleCss = css;
  
  // For each external url import add a link reference
  while((match = importMatcher.exec(css)) !== null) {
    styleCss = styleCss.replace(match[0], "");
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = match[2];
    if (match[1]) {
      link.media = match[1];
    }
    // For target document append to head else append to target
    if (target === document) {
      document.head.appendChild(link);
    } else {
      target.appendChild(link);
    }
  };
  return styleCss;
};

// target: Document | ShadowRoot
export const injectGlobalCss = (css, target, first) => {
  
  const sheet = new CSSStyleSheet();
  sheet.replaceSync(createLinkReferences(css,target));
  if (first) {
    target.adoptedStyleSheets = [sheet, ...target.adoptedStyleSheets];
  } else {
    target.adoptedStyleSheets = [...target.adoptedStyleSheets, sheet];
  }
};

const addCssBlock = function (block, before = false) {
  const tpl = document.createElement("template");
  tpl.innerHTML = block;
  document.head[before ? "insertBefore" : "appendChild"](
    tpl.content,
    document.head.firstChild
  );
};

const addStyleInclude = (module, target) => {
  addCssBlock(`<custom-style><style include="${module}"></style></custom-style>`, true);
};

const getStyleModule = (id) => {
  const template = DomModule.import(id, "template");
  const cssText =
    template &&
    stylesFromTemplate(template, "")
      .map((style) => style.textContent)
      .join(" ");
  return cssText;
};
import stylesCss from './styles.css';
import '@vaadin/vaadin-lumo-styles/color.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import averageRatingsCss from './components/average-ratings.css';
import button_switch_themeCss from './components/button_switch_theme.css';
import menuBarButtonCss from './components/menu-bar-button.css';
import ownDriveOffersEditDialogCss from './components/own-drive-offers-edit-dialog.css';
import profileDoubleRatingsCss from './components/profile-double-ratings.css';
import searchDriveResultViewDialogCss from './components/search-drive-result-view-dialog.css';

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow['_vaadintheme_mitfahrgelegenheit_globalCss'] = window.Vaadin.Flow['_vaadintheme_mitfahrgelegenheit_globalCss'] || [];
export const applyTheme = (target) => {
  
  const injectGlobal = (window.Vaadin.Flow['_vaadintheme_mitfahrgelegenheit_globalCss'].length === 0) || (!window.Vaadin.Flow['_vaadintheme_mitfahrgelegenheit_globalCss'].includes(target) && target !== document);
  if (injectGlobal) {
    injectGlobalCss(stylesCss.toString(), target);
    
    window.Vaadin.Flow['_vaadintheme_mitfahrgelegenheit_globalCss'].push(target);
  }
  if (!document['_vaadintheme_mitfahrgelegenheit_componentCss']) {
    registerStyles(
      'average-ratings',
      css`
        ${unsafeCSS(averageRatingsCss.toString())}
      `
    );
    registerStyles(
      'button_switch_theme',
      css`
        ${unsafeCSS(button_switch_themeCss.toString())}
      `
    );
    registerStyles(
      'menu-bar-button',
      css`
        ${unsafeCSS(menuBarButtonCss.toString())}
      `
    );
    registerStyles(
      'own-drive-offers-edit-dialog',
      css`
        ${unsafeCSS(ownDriveOffersEditDialogCss.toString())}
      `
    );
    registerStyles(
      'profile-double-ratings',
      css`
        ${unsafeCSS(profileDoubleRatingsCss.toString())}
      `
    );
    registerStyles(
      'search-drive-result-view-dialog',
      css`
        ${unsafeCSS(searchDriveResultViewDialogCss.toString())}
      `
    );
    
    document['_vaadintheme_mitfahrgelegenheit_componentCss'] = true;
  }
  // Lumo styles are injected into shadow roots.
// For the document, we need to be compatible with flow-generated-imports and add missing <style> tags.
const shadowRoot = (target instanceof ShadowRoot);
if (shadowRoot) {
injectGlobalCss(getStyleModule("lumo-color"), target, true);
injectGlobalCss(getStyleModule("lumo-typography"), target, true);
} else if (!document['_vaadinthemelumoimports_']) {
addStyleInclude("lumo-color", target);
addStyleInclude("lumo-typography", target);
document['_vaadinthemelumoimports_'] = true;
}

}
