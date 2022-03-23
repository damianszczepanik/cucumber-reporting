document.addEventListener('DOMContentLoaded', (event) => {
  // included highlight.js script is trimmed to only support these languages
  // visit https://highlightjs.org/ for more information
  let fileTypes = [ '.xml', '.json' ];

  fileTypes.forEach((ext) => {
    document.querySelectorAll(`a[href\$='${ext}']`).forEach((el) => {
      hljs.highlightElement(el.parentElement.parentElement.querySelector('pre'));
    });
  });
});
