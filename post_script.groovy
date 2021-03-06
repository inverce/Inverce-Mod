/**
 *
 * To publish all libraries with new version
 *  1. Change libraryVersion in project.gradle
 *  2. Clean project
 *  3. assembleRelease on root project
 *  4. bintrayUpload on root project
 *
 * INFO this file expects those properties to be set.
 *
 * Under your home gradle.properties (~/.gradle/gradle.properties)
 * <code>
 *      # Bintray user and apiKey.
 *      bintrayUser={user}*      bintrayApiKey={apiKey}* </code>
 *
 * Under project build.gradle
 *
 * <code>
 *      ext {*          bintray = [
 *              repository : "{your_repository_name}",
 *              libraryVersion : "{version_for_all_libraries}",
 *              groupId: "{group_id}",
 *              vcsUrl: "{url_to_vcs}"
 *          ]
 *}*
 *      apply from: '{link_to_this_file}'
 * </code>
 *
 * Lastly under your module build.gradle
 * <code>
 *
 *      plugins { id "com.jfrog.bintray" version "1.8.4" }*      plugins { id 'maven-publish' }*
 *      ...
 *       ext {*          artifactId = "{artifactId}"
 *}* </code>
 */


def global(String propertyName) {
    if (hasProperty(propertyName)) {
        return "${propertyName}"
    }
    return null
}


def bUser = ""
if (hasProperty("bintrayUser")) {
    bUser = "${bintrayUser}"
}
def bApi = ""
if (hasProperty("bintrayApiKey")) {
    bApi = "${bintrayApiKey}"
}

bintray {
    user = bUser
    key = bApi
    pkg {
        repo = rootProject.ext.bintray.repository
        name = project.getName()
        userOrg = bUser
        licenses = ['Apache-2.0']
        vcsUrl = rootProject.ext.bintray.vcsUrl
        issueTrackerUrl = rootProject.ext.bintray.vcsUrl
        publish = true
        override = true
        version {
            name = rootProject.ext.bintray.libraryVersion
            desc = ''
            released = new Date()
            vcsTag = rootProject.ext.bintray.libraryVersion
        }

        publications = ['MyPublication']

    }
}

task sourceJar(type: Jar) {
    from android.sourceSets.main.java.srcDirs
    classifier "sources"
}

task androidJavadocsJar(type: Jar, dependsOn: dokka) {
    classifier = 'javadoc'
    from "$buildDir/javadoc"
}

publishing {
    publications {

        MyPublication(MavenPublication) {
            groupId rootProject.ext.bintray.groupId
            artifactId project.artifactId
            version rootProject.ext.bintray.libraryVersion

            artifact "$buildDir/outputs/aar/${project.getName()}-release.aar"
            artifact sourceJar
            artifact androidJavadocsJar

            pom.withXml {
                def dependenciesNode = asNode().appendNode('dependencies')

                // Iterate over the implementation dependencies (we don't want the test ones), adding a <dependency> node for each
                configurations.implementation.allDependencies.each {
                    // Ensure dependencies such as fileTree are not included.
                    if (it.name != 'unspecified') {
                        if (it.group == rootProject.getName()) {
                            def dependencyNode = dependenciesNode.appendNode('dependency')
                            dependencyNode.appendNode('groupId', rootProject.ext.bintray.groupId)
                            dependencyNode.appendNode('artifactId', it.name)
                            dependencyNode.appendNode('version', rootProject.ext.bintray.libraryVersion)
                        } else {
                            def dependencyNode = dependenciesNode.appendNode('dependency')
                            dependencyNode.appendNode('groupId', it.group)
                            dependencyNode.appendNode('artifactId', it.name)
                            dependencyNode.appendNode('version', it.version)
                        }
                    }
                }
            }
        }
    }
}

afterEvaluate {
    // make sure to generate pom before publishing to bintray
    bintrayUpload.dependsOn 'generatePomFileForMyPublicationPublication'
    bintrayUpload.dependsOn 'androidJavadocsJar'
    bintrayUpload.dependsOn 'assembleRelease'
    bintrayUpload.dependsOn 'sourceJar'
}