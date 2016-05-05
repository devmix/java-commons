# commons-i18n

Localization which supports plural forms, numbers, capitalization for first letter, short names

## Resource bundle

### Example
```
simple.attribute-1=value${( plural : 's', numeric: ['','s','s2'], short:'a', case: 'A' )}
simple.attribute-2=%s ${../attribute-1}
simple.attribute-3=%s ${../attribute-1()}
simple.attribute-4=%s ${../attribute-1(p:true)}
```

`${( ... )}` - defining parameters of value

* `plural | p` - letter for plural form, result will be <value><letter>
* `numeric | n` - plural form for a number (http://localization-guide.readthedocs.org/en/latest/l10n/pluralforms.html)
* `short | s` - short name of term
* `cap | c` - value of capitalized letter(s)

`${ ... }` - lookup for attribute by reference, ex. `simple.attribute-1=${../attribute-1}`

* `../attribute-1` -> simple.attribute-1
* `/attribute-1` -> attribute-1
* `sub-attribute-1` -> simple.attribute-1.sub-attribute-1
* `../attribute-1()` - for value found by reference will be used parameters like plural forms, short name and etc.
* `../attribute-1(p:true)` - like previous but with predefined value of `plural` parameter

## Code example

Properties
```
attribute-1=value${( plural : 's', numeric: ['','s','s2'], short:'a', case: 'A' )}
```

Java
```
    Language lang = LanguageFactory.create(
        new ResourceBundleDataSource("lang-resource-bundle"),
        LanguageOptions.create());

    lang.of("attribute-1", ArgumentsFactory.plural()); // values
    lang.of("attribute-1", ArgumentsFactory.numeric(11)); // values2 for russian locale
```
See tests for examples of code