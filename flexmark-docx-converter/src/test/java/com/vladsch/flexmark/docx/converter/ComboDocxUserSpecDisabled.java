package com.vladsch.flexmark.docx.converter;

import com.vladsch.flexmark.test.spec.SpecExample;
import org.jetbrains.annotations.NotNull;
import org.junit.runners.Parameterized;

import java.util.List;

public class ComboDocxUserSpecDisabled extends ComboDocxConverterSpecTestBase {
    private static final String SPEC_RESOURCE = "/docx_user_ast_spec.md";
    private static final String FILE_TEST_CASE_DUMP_LOCATION = "/flexmark-docx-converter/src/test/resources/docx_user_ast_spec/";

    public ComboDocxUserSpecDisabled(SpecExample example) {
        super(example, null);
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> data() {
        return getTestData(SPEC_RESOURCE);
    }

    @NotNull
    @Override
    public String getSpecResourceName() {
        return SPEC_RESOURCE;
    }

    @NotNull
    @Override
    public String getProjectRootDirectory() {
        return PROJECT_ROOT_DIRECTORY;
    }

    @NotNull
    @Override
    public String getFileTestCaseDumpLocation() {
        return FILE_TEST_CASE_DUMP_LOCATION;
    }
}
