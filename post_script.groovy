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
 *      bintrayUser={user}
 *      bintrayApiKey={apiKey}
 * </code>
 *
 * Under project build.gradle
 *
 * <code>
 *      ext {
 *          bintray = [
 *              repository : "{your_repository_name}",
 *              libraryVersion : "{version_for_all_libraries}",
 *              groupId: "{group_id}",
 *              vcsUrl: "{url_to_vcs}"
 *          ]
 *      }
 *
 *      apply from: '{link_to_this_file}'
 * </code>
 *
 * Lastly under your module build.gradle
 * <code>
 *
 *      plugins { id "com.jfrog.bintray" version "1.8.4" }
 *      plugins { id 'maven-publish' }
 *
 *      ...
 *       ext {
 *          artifactId = "{artifactId}"
 *       }
 * </code>
 */

bintray {
    user = "${bintrayUser}"
    key = "${bintrayApiKey}"
    pkg {
        repo = rootProject.ext.bintray.repository
        name = project.getName()
        userOrg = "${bintrayUser}"
        licenses = ['Apache-2.0']
        vcsUrl = rootProject.ext.bintray.vcsUrl
        publish = true
        version {
            name = rootProject.ext.bintray.libraryVersion
            desc = ''
            released  = new Date()
            vcsTag = rootProject.ext.bintray.libraryVersion
        }

        publications = ['MyPublication']

    }
}

publishing {
    publications {
        MyPublication(MavenPublication) {
            groupId rootProject.ext.bintray.groupId
            artifactId project.artifactId
            version rootProject.ext.bintray.libraryVersion

            artifact "$buildDir/outputs/aar/${project.getName()}-release.aar"

            pom.withXml {
                def dependenciesNode = asNode().appendNode('dependencies')

                // Iterate over the implementation dependencies (we don't want the test ones), adding a <dependency> node for each
                configurations.implementation.allDependencies.each {
                    // Ensure dependencies such as fileTree are not included.
                    if (it.name != 'unspecified') {
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

afterEvaluate {
    // make sure to generate pom before publishing to bintray
    bintrayUpload.dependsOn 'generatePomFileForMyPublicationPublication'
}