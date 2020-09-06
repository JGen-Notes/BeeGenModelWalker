/**
 * [The "BSD license"]
 * Copyright (c) 2016, JGen Notes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions 
 *    and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS 
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package eu.jgen.beegen.model.mirror.decaration;

import java.util.ArrayList;

public class Views extends Node  {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<SimpleView> simpleViews;
	
	private ArrayList<GroupView>groupViews;

	public Views() {
		super();
	}

	public Views( ArrayList<SimpleView> simpleViews, ArrayList<GroupView>groupViews) {
		super();
		this.simpleViews = simpleViews;
	}

	public ArrayList<GroupView> getGroupViews() {
		if (groupViews == null) {
			groupViews = new ArrayList<GroupView>();
		}
		return groupViews;
	}

	public ArrayList<SimpleView> getSimpleViews() {
		if (simpleViews == null) {
			simpleViews =  new ArrayList<SimpleView>();
		}
		return simpleViews;
	}

	public void setGroupViews(ArrayList<GroupView> groupViews) {
		this.groupViews = groupViews;
	}

	public void setSimpleViews(ArrayList<SimpleView> simpleViews) {
		this.simpleViews = simpleViews;
	}	
	

}
