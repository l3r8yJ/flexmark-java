package com.vladsch.flexmark.ext.admonition;

import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.test.spec.SpecExample;
import com.vladsch.flexmark.test.util.ComboSpecTestCase;
import com.vladsch.flexmark.test.util.FlexmarkSpecExampleRenderer;
import com.vladsch.flexmark.test.util.SpecExampleRenderer;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboAdmonitionFormatterSpecTest extends ComboSpecTestCase {
    private static final String SPEC_RESOURCE = "/ext_admonition_formatter_spec.md";
    private static final DataHolder OPTIONS = new MutableDataSet()
            //.set(FormattingRenderer.INDENT_SIZE, 2)
            //.set(HtmlRenderer.PERCENT_ENCODE_URLS, true)
            .set(Parser.EXTENSIONS, Collections.singleton(AdmonitionExtension.create()))
            .set(Parser.LISTS_AUTO_LOOSE, false)
            .set(Parser.BLANK_LINES_IN_AST, true);

    private static final Map<String, DataHolder> optionsMap = new HashMap<>();
    static {
        //optionsMap.put("left-align-marker-remove", new MutableDataSet().set(TablesExtension.FORMAT_TABLE_LEFT_ALIGN_MARKER, DiscretionaryText.REMOVE));
    }
    public ComboAdmonitionFormatterSpecTest(SpecExample example) {
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
        DataHolder combinedOptions = combineOptions(OPTIONS, exampleOptions);
        return new FlexmarkSpecExampleRenderer(example, combinedOptions, Parser.builder(combinedOptions).build(), Formatter.builder(combinedOptions).build(), true);
    }
}
