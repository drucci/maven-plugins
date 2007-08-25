package org.apache.maven.plugins.stage;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.wagon.WagonException;
import org.apache.maven.wagon.repository.Repository;
import org.codehaus.plexus.util.StringUtils;

import java.io.IOException;

/**
 * Copies artifacts from one repository to the another repository.
 * 
 * @author Jason van Zyl
 * @requiresProject false
 * @goal copy
 */
public class CopyRepositoryMojo
    extends AbstractMojo
{
    /**
     * The URL to the source repository.
     *
     * @parameter expression="${source}"
     */
    private String source;

    /**
     * The URL to the target repository.
     * 
     * @parameter expression="${target}"
     */
    private String target;

    /**
     * The id of the target repository.
     * 
     * @parameter expression="${repositoryId}" default-value="target"
     */
    private String repositoryId;

    /**
     * The plugin doesn't currently read username/password from settings.xml.
     * If your local username is different than your Apache username, you can
     * specify your Apache username with this parameter.
     *
     * @parameter expression="${stage.username}"
     */
    private String username;

    /**
     * The version of the artifact that is to be copied.
     * <p>
     * <b>Note:</b> This is currently only used for naming temporary files.
     * <i>All</i> versions of the artifacts will be copied.
     * </p>
     *
     * @parameter expression="${version}"
     * @required
     */
    private String version;

    /**
     * The repository copier to use.
     *
     * @component
     */
    private RepositoryCopier copier;

    public void execute()
        throws MojoExecutionException
    {
        try
        {
            Repository targetRepository = new Repository( repositoryId, target );
            getLog().debug( "username: " + username );
            if ( StringUtils.isEmpty( username ) )
            {
                copier.copy( source, targetRepository, version );
            }
            else
            {
                copier.copy( source, targetRepository, version, username );
            }
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException(
                "Error copying repository from " + source + " to " + target, e );
        }
        catch ( WagonException e )
        {
            throw new MojoExecutionException(
                "Error copying repository from " + source + " to " + target, e );
        }
    }
}

