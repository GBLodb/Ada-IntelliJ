package com.adacore.adaintellij.misc;

import com.adacore.adaintellij.lsp.AdaLSPDriverListener;
import com.adacore.adaintellij.project.AdaProjectListener;
import com.adacore.adaintellij.project.GPRFileManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AdaFoldingBuilderTest extends LightJavaCodeInsightFixtureTestCase {

    Module myModule;

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/ada-sources";
    }

    @BeforeEach
    public void  setup() throws Exception
    {
        super.setUp();
    }

    @Test
    public void testFolds()
    {
        Project project = myFixture.getProject();

        myFixture.copyFileToProject( "/project.gpr");

        project.getComponent(AdaProjectListener.class).projectOpened(project);
        project.getComponent(AdaLSPDriverListener.class).projectOpened(project);
        project.getComponent(GPRFileManagerListener.class).projectOpened(project);

        myFixture.testFoldingWithCollapseStatus(getTestDataPath() + "/folding-test-data.ads");
    }

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new LightProjectDescriptor(){
            protected VirtualFile createSourceRoot(@NotNull Module module, String srcPath) {

                String fp = myFixture.getTempDirFixture().getTempDirPath();

                VirtualFile srcRootDirVF = VirtualFileManager
                    .getInstance()
                    .refreshAndFindFileByUrl(
                        "file://" + myFixture.getTempDirFixture()
                            .getTempDirPath()
                    );

                assert srcRootDirVF != null;
                srcRootDirVF.refresh( false, false );
                VirtualFile srcRootVF = null;
                try {
                    srcRootVF = srcRootDirVF.createChildDirectory(this, srcPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                registerSourceRoot( module.getProject(), srcRootVF);
                return srcRootVF;
            }
        };
    }
}
