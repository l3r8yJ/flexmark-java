package com.vladsch.flexmark.html2md.converter;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.test.spec.SpecExample;
import com.vladsch.flexmark.test.spec.SpecReader;
import com.vladsch.flexmark.test.util.ComboSpecTestCase;
import com.vladsch.flexmark.test.util.FlexmarkSpecExampleRenderer;
import com.vladsch.flexmark.test.util.SpecExampleRenderer;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboAppHtmlAttributeConverterTest extends ComboSpecTestCase {
    private static final String SPEC_RESOURCE = "/app_html_attribute_converter_spec.md";
    private static final DataHolder OPTIONS = new MutableDataSet()
            .set(HtmlRenderer.INDENT_SIZE, 2)
            .set(FlexmarkHtmlConverter.OUTPUT_ATTRIBUTES_ID, true)
            //.set(HtmlRenderer.PERCENT_ENCODE_URLS, true)
            //.set(Parser.EXTENSIONS, Collections.singleton(FlexmarkHtmlConverter.create())
            ;

    private static final Map<String, DataHolder> optionsMap = new HashMap<>();
    static {
        optionsMap.put("paren-lists", new MutableDataSet().set(FlexmarkHtmlConverter.DOT_ONLY_NUMERIC_LISTS, false));
        optionsMap.put("output-unknown", new MutableDataSet().set(FlexmarkHtmlConverter.OUTPUT_UNKNOWN_TAGS, true));
        optionsMap.put("nbsp", new MutableDataSet().set(FlexmarkHtmlConverter.NBSP_TEXT, "&nbsp;"));
        optionsMap.put("no-quotes", new MutableDataSet().set(FlexmarkHtmlConverter.TYPOGRAPHIC_QUOTES, false));
        optionsMap.put("no-smarts", new MutableDataSet().set(FlexmarkHtmlConverter.TYPOGRAPHIC_SMARTS, false));
        optionsMap.put("wrap-autolinks", new MutableDataSet().set(FlexmarkHtmlConverter.WRAP_AUTO_LINKS, true));
        optionsMap.put("no-autolinks", new MutableDataSet().set(FlexmarkHtmlConverter.EXTRACT_AUTO_LINKS, false));
        optionsMap.put("src-pos", new MutableDataSet().set(HtmlRenderer.SOURCE_POSITION_ATTRIBUTE, "md-pos"));
        optionsMap.put("div-as-para", new MutableDataSet().set(FlexmarkHtmlConverter.DIV_AS_PARAGRAPH, true));
        optionsMap.put("no-br-as-para-breaks", new MutableDataSet().set(FlexmarkHtmlConverter.BR_AS_PARA_BREAKS, false));
        optionsMap.put("no-br-as-extra-blank-lines", new MutableDataSet().set(FlexmarkHtmlConverter.BR_AS_EXTRA_BLANK_LINES, false));
        optionsMap.put("skip-heading-1", new MutableDataSet().set(FlexmarkHtmlConverter.SKIP_HEADING_1, true));
        optionsMap.put("skip-heading-2", new MutableDataSet().set(FlexmarkHtmlConverter.SKIP_HEADING_2, true));
        optionsMap.put("skip-heading-3", new MutableDataSet().set(FlexmarkHtmlConverter.SKIP_HEADING_3, true));
        optionsMap.put("skip-heading-4", new MutableDataSet().set(FlexmarkHtmlConverter.SKIP_HEADING_4, true));
        optionsMap.put("skip-heading-5", new MutableDataSet().set(FlexmarkHtmlConverter.SKIP_HEADING_5, true));
        optionsMap.put("skip-heading-6", new MutableDataSet().set(FlexmarkHtmlConverter.SKIP_HEADING_6, true));
        optionsMap.put("skip-attributes", new MutableDataSet().set(FlexmarkHtmlConverter.SKIP_ATTRIBUTES, true));
    }
    public ComboAppHtmlAttributeConverterTest(SpecExample example) {
        super(example);
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> data() {
        return getTestData(SPEC_RESOURCE);
    }

    @Nullable
    @Override
    public DataHolder options(String option) {
        return optionsMap.get(option);
    }

    @NotNull
    @Override
    public String getSpecResourceName() {
        return SPEC_RESOURCE;
    }

    @Override
    public @NotNull SpecExampleRenderer getSpecExampleRenderer(@NotNull SpecExample example, @Nullable DataHolder exampleOptions) {
        DataHolder combineOptions = combineOptions(OPTIONS, exampleOptions);
        return new FlexmarkSpecExampleRenderer(example, combineOptions, new HtmlConverter(combineOptions), new HtmlRootNodeRenderer(combineOptions), true);
    }

    @NotNull
    @Override
    public SpecReader create(@NotNull InputStream inputStream, @Nullable String fileUrl) {
        dumpSpecReader = new HtmlSpecReader(inputStream, this, fileUrl);
        return dumpSpecReader;
    }

    @Override
    protected void assertRendering(@Nullable String fileUrl, @NotNull String source, @NotNull String expectedHtml, @Nullable String expectedAst, @Nullable String optionsSet) {
        // reverse source and html
        super.assertRendering(fileUrl, expectedHtml, source, expectedAst, optionsSet);
    }
}
