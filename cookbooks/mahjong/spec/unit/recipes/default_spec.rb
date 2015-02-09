#
# Cookbook Name:: mahjong
# Spec:: default
#
# Copyright (c) 2015 The Authors, All Rights Reserved.

require 'spec_helper'

describe 'mahjong::default' do

  context 'When all attributes are default, on an unspecified platform' do

    let(:chef_run) do
      runner = ChefSpec::Runner.new
      runner.converge(described_recipe)
    end

    it 'should include java recipe' do
      expect(chef_run).to include_recipe('java::default')
    end

    it 'should include tomcat recipe' do
      expect(chef_run).to include_recipe('tomcat::default')
    end

    it 'should install tomcat server' do
      expect(chef_run).to install_package('tomcat')
    end

  end
end
